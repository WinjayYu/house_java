package com.ryel.zaja.controller.api.pingan;

import com.ecc.emp.data.KeyedCollection;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.PinganApiEnum;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.entity.UserWalletAccount;
import com.ryel.zaja.pingan.PinganUtils;
import com.ryel.zaja.pingan.WalletConstant;
import com.ryel.zaja.pingan.ZJJZ_API_GW;
import com.ryel.zaja.service.PinganApiLogService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.service.UserWalletAccountService;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.VerifyCodeUtil;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController()
@RequestMapping(value = "/api/pingan/wallet",produces = "application/json; charset=UTF-8")
public class WalletApi {
    protected final static Logger logger = LoggerFactory.getLogger(WalletApi.class);
    @Autowired
    private UserService userService;
    @Autowired
    private PinganApiLogService pinganApiLogService;
    @Autowired
    private UserWalletAccountService userWalletAccountService;

    /**
     * 创建见证宝账户
     * @param userId 用户id
     */
    @RequestMapping(value = "createaccount")
    public Result createaccount(Integer userId) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            // 校验用户信息
            if(userId == null){
                logger.info("userId:" + userId);
                return Result.error().msg(Error_code.ERROR_CODE_0041).data(new HashMap<>());
            }
            User user = userService.findById(userId);
            if(user == null){
                logger.info("user is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            String mobile = user.getMobile();
            String nickname = user.getNickname();
            if(StringUtils.isBlank(mobile) || mobile.length() != 11 || StringUtils.isBlank(nickname)){
                logger.info("user:" + JsonUtil.obj2Json(user));
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6000"); // 交易码，此处以【6000】接口为例子
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
            parmaKeyDict.put("FuncFlag", "1"); // 功能标志1：开户
            parmaKeyDict.put("ThirdCustId", user.getId()); // 交易网会员代码
            parmaKeyDict.put("CustProperty", "00"); // 会员属性
            parmaKeyDict.put("NickName", nickname); // 会员昵称
            parmaKeyDict.put("MobilePhone", mobile); // 手机号码
            parmaKeyDict.put("Email", ""); // 邮箱
            parmaKeyDict.put("Reserve", "会员开户"); // 保留域

            System.out.println("请求报文==============" + parmaKeyDict);

            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文

            System.out.println("第一部分：生成发送银行的请求的报文的实例");
            System.out.println(tranMessage);
            System.out.println("-------------------------------");

            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文

            System.out.println("第二部分：获取银行返回的报文");
            System.out.println(recvMessage);
            System.out.println("-------------------------------");

            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String custAcctId = (String) retKeyDict.get("CustAcctId");
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode) && StringUtils.isNotEmpty(custAcctId)) {
                // 创建成功，写入数据库
                UserWalletAccount userWalletAccount = new UserWalletAccount();
                userWalletAccount.setUserId(userId);
                userWalletAccount.setThirdCustId(userId.toString());
                userWalletAccount.setCustAcctId(custAcctId);
                userWalletAccount.setMobilePhone(mobile);
                userWalletAccount.setNickName(nickname);
                userWalletAccountService.create(userWalletAccount);
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.CREATE_ACCOUNT,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),userId);
        }
    }

    /**
     * 查询见证宝余额信息
     * @param userId 用户id
     */
    @RequestMapping(value = "getWalletBalanceInfo")
    public Result getWalletInfo(Integer userId) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            // 校验用户信息
            if(userId == null){
                logger.info("userId:" + userId);
                return Result.error().msg(Error_code.ERROR_CODE_0041).data(new HashMap<>());
            }
            UserWalletAccount userWalletAccount = userWalletAccountService.findByUserId(userId);
            if(userWalletAccount == null){
                logger.info("userWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6037");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId",WalletConstant.SUP_ACCT_ID);
            parmaKeyDict.put("ThirdCustId", userWalletAccount.getThirdCustId());
            parmaKeyDict.put("Reserve", "1");

            System.out.println("请求报文==============" + parmaKeyDict);

            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文

            System.out.println("第一部分：生成发送银行的请求的报文的实例");
            System.out.println(tranMessage);
            System.out.println("-------------------------------");

            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文

            System.out.println("第二部分：获取银行返回的报文");
            System.out.println(recvMessage);
            System.out.println("-------------------------------");

            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            /**
             * 返回retKeyDict包含如下字段：
             * 子账户账号	   CustAcctId	C(32)	必输
             * 子账户可提现余额	TotalAmount	9(15)	必输
             * 子账户可用余额	TotalBalance	9(15)	必输
             * 子账户冻结金额	TotalFreezeAmount	9(15)	必输	指在担保子账户里待支付或冻结的金额
             * 保留域	Reserve	C(120)	可选
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                return Result.success().data(retKeyDict);
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.CREATE_ACCOUNT,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),userId);
        }
    }

}
