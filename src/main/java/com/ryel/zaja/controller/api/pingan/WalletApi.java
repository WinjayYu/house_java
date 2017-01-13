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
    public Result createAccount(Integer userId) {
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
    @RequestMapping(value = "getwalletbalanceinfo")
    public Result getWalletBalanceInfo(Integer userId) {
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
            pinganApiLogService.create(PinganApiEnum.GET_WALLET_BALANCE_INFO,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),userId);
        }
    }

    /**
     * 绑定提现账户
     * @param userId 用户id
     * @param idCard 身份证号
     * @param acctId 银行卡号
     * @param bankType 银行类型
     * @param bankCode 银行code
     * @param bankName 银行名称
     */
    @RequestMapping(value = "binddrawcard")
    public Result bindDrawCard(Integer userId,String idCard,String acctId,String bankType,String bankCode,String bankName) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            logger.info("入参:" + "userId=" + userId + ",idCard=" + idCard + ",acctId=" + acctId + ",bankType="
                    + bankType + ",bankCode=" + bankCode + ",bankName=" + bankName);
            // 参数校验
            if(userId == null || StringUtils.isBlank(idCard) || StringUtils.isBlank(acctId)
                    || StringUtils.isBlank(bankType) || StringUtils.isBlank(bankCode) || StringUtils.isBlank(bankName)){
                // 必填参数为空
                return Result.error().msg(Error_code.ERROR_CODE_0045).data(new HashMap<>());
            }
            if(!"1".equals(bankType) && !"2".equals(bankType)){
                // bankType错误
                return Result.error().msg(Error_code.ERROR_CODE_0046).data(new HashMap<>());
            }
            // 校验用户信息
            User user = userService.findById(userId);
            if(user == null){
                logger.info("user is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            UserWalletAccount userWalletAccount = userWalletAccountService.findByUserId(userId);
            if(userWalletAccount == null){
                logger.info("userWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6066");
            // 企业代码
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            // 资金汇总账号
            parmaKeyDict.put("SupAcctId",WalletConstant.SUP_ACCT_ID);
            // 子账户账号
            parmaKeyDict.put("CustAcctId", userWalletAccount.getCustAcctId());
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", userWalletAccount.getThirdCustId());
            // 会员名称
            parmaKeyDict.put("CustName", user.getNickname());
            // 会员证件类型
            parmaKeyDict.put("IdType", "1");
            // 会员证件号码
            parmaKeyDict.put("IdCode", idCard);
            // 会员账号（银行卡号）
            parmaKeyDict.put("AcctId", acctId);
            // 银行类型  1：本行 2：他行
            parmaKeyDict.put("BankType", bankType);
            // 开户行名称
            parmaKeyDict.put("BankName", bankName);
            // 超级网银行号
            parmaKeyDict.put("BankCode", bankCode);
            // 手机号
            parmaKeyDict.put("MobilePhone", user.getMobile());
            // 保留域
            parmaKeyDict.put("Reserve", "");

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
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                userWalletAccount.setCustName(user.getNickname());
                userWalletAccount.setAcctId(acctId);
                userWalletAccount.setBankType(bankType);
                userWalletAccount.setBankCode(bankCode);
                userWalletAccount.setBankName(bankName);
                userWalletAccountService.update(userWalletAccount);
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0047).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0047).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.BIND_WITHDRAW_CARD,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),userId);
        }
    }

    /**
     * 绑定提现账户前发送短信验证码
     * @param userId 用户id
     */
    @RequestMapping(value = "sendmsg4bindcard")
    public Result sendMsg4BindCard(Integer userId,String messageCode) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            logger.info("入参:" + "userId=" + userId + ",messageCode=" + messageCode);
            // 参数校验
            if(userId == null || StringUtils.isBlank(messageCode)){
                // 必填参数为空
                return Result.error().msg(Error_code.ERROR_CODE_0045).data(new HashMap<>());
            }
            // 校验用户信息
            User user = userService.findById(userId);
            if(user == null){
                logger.info("user is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            UserWalletAccount userWalletAccount = userWalletAccountService.findByUserId(userId);
            if(userWalletAccount == null){
                logger.info("userWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6067");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 子账户账号
            parmaKeyDict.put("CustAcctId", userWalletAccount.getCustAcctId());
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", userWalletAccount.getThirdCustId());
            // 会员账号
            parmaKeyDict.put("AcctId", userWalletAccount.getAcctId());
            // 短信验证码
            parmaKeyDict.put("MessageCode", messageCode);
            // 保留域
            parmaKeyDict.put("Reserve", "");

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
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                // 短信验证成功，绑卡成功
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0048).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0048).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.SEND_MSG_FOR_BIND_WITHDRAW_CARD,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),userId);
        }
    }

}

