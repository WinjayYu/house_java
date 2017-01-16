package com.ryel.zaja.controller.api.pingan;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.PinganApiEnum;
import com.ryel.zaja.config.enums.TradeRecordStatus;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.pingan.PinganUtils;
import com.ryel.zaja.pingan.WalletConstant;
import com.ryel.zaja.pingan.ZJJZ_API_GW;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private TradeRecordService tradeRecordService;
    @Autowired
    private SuperBankInfoService superBankInfoService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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
     * 功能子账户余额查询
     */
    @RequestMapping(value = "getcommonbalanceinfo")
    public Result getCommonBalanceInfo() {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {

            parmaKeyDict.put("TranFunc", "6010"); // 交易码，此处以【6000】接口为例子
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
//		parmaKeyDict.put("CustAcctId", WalletConstant.COMMON_ACCT_ID); // 资金汇总账号
            parmaKeyDict.put("SelectFlag", "3"); // 1：全部 2：普通会员子账号 3：功能子账号
            parmaKeyDict.put("PageNum", "1"); // 交易网会员代码
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

            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                String TotalCount =(String)retKeyDict.get("TotalCount");
                Integer iCount = Integer.valueOf(TotalCount);
                String ArrayContent =(String)retKeyDict.get("ArrayContent"); //ArrayContent为固定名称。
                String [] array=ArrayContent.split("&");

//		子账户	CustAcctId	C(32)	必输	可重复
//		子账户属性	CustType	C(1)	必输	可重复（1：普通会员子账号 2：挂账子账号  3：手续费子账号 4：利息子账号5：平台担保子账号）
//		交易网会员代码	ThirdCustId	C(32)	必输	可重复
//		子账户名称	CustName	C(120)	必输	可重复
//		账户可用余额	TotalBalance	9(15)	必输	可重复
//		账户可提现金额	TotalTranOutAmount	9(15)	必输	可重复
//		维护日期	TranDate	C(8)	必输	可重复（开户日期或修改日期）
                String[] CustAcctId = new String[iCount];
                String[] CustType = new String[iCount];
                String[] ThirdCustId = new String[iCount];
                String[] CustName = new String[iCount];
                String[] TotalBalance = new String[iCount];
                String[] TotalTranOutAmount = new String[iCount];
                String[] TranDate = new String[iCount];
                int i;
                int j;

                for(i=0,j=0;i<35;i=i+7,j++)
                {
                    CustAcctId[j]=array[i];
                    CustType[j]=array[i+1];
                    ThirdCustId[j]=array[i+2];
                    CustName[j]=array[i+3];
                    TotalBalance[j]=array[i+4];
                    TotalTranOutAmount[j]=array[i+5];
                    TranDate[j]=array[i+6];
                }
                System.out.println("CustAcctId:" + CustAcctId);
                System.out.println("CustType:" + CustType);
                System.out.println("ThirdCustId:" + ThirdCustId);
                System.out.println("CustName:" + CustName);
                System.out.println("TotalBalance:" + TotalBalance);
                System.out.println("TotalTranOutAmount:" + TotalTranOutAmount);
                System.out.println("TranDate:" + TranDate);

                List<CommonAccountInfo> commonAccountInfoList = new ArrayList<CommonAccountInfo>();
                for (int o = 0; o < CustAcctId.length; o++) {
                    System.out.println(CustName[o] + "   " + CustAcctId[o] + "   " + TotalBalance[o] + "   " + TotalTranOutAmount[o]);
                    CommonAccountInfo commonAccountInfo = new CommonAccountInfo();
                    commonAccountInfo.setCustAcctId(CustAcctId[o]);
                    commonAccountInfo.setCustName(CustName[o]);
                    commonAccountInfo.setCustType(CustType[o]);
                    commonAccountInfo.setThirdCustId(ThirdCustId[o]);
                    commonAccountInfo.setTotalBalance(TotalBalance[o]);
                    commonAccountInfo.setTotalTranOutAmount(TotalTranOutAmount[o]);
                    commonAccountInfoList.add(commonAccountInfo);
                }
                return Result.success().data(commonAccountInfoList);
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
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
    /**
     * 付款第一步：付款到担保账户
     * @param outUserId 用户id  付款用户id
     * @param inUserId 用户id 收款用户id
     * @param messageCode 短信验证码
     */
    @RequestMapping(value = "outtocommon")
    public Result outToCommon(Integer outUserId,Integer inUserId,String messageCode,String tranAmount) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            logger.info("入参:" + "outUserId=" + outUserId + ",inUserId=" + inUserId+ ",messageCode=" + messageCode);
            // 参数校验
            if(outUserId == null || inUserId == null || StringUtils.isBlank(messageCode)){
                // 必填参数为空
                return Result.error().msg(Error_code.ERROR_CODE_0045).data(new HashMap<>());
            }
            // 校验用户信息
            User outUser = userService.findById(outUserId);
            User inUser = userService.findById(inUserId);
            if(outUser == null){
                logger.info("outUser is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            if(inUser == null){
                logger.info("inUser is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            UserWalletAccount outUserWalletAccount = userWalletAccountService.findByUserId(outUserId);
            UserWalletAccount inUserWalletAccount = userWalletAccountService.findByUserId(inUserId);
            if(outUserWalletAccount == null){
                logger.info("outUserWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }
            if(inUserWalletAccount == null){
                logger.info("inUserWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            String OutCustAcctId = outUserWalletAccount.getCustAcctId();
            String OutThirdCustId = outUserWalletAccount.getThirdCustId();
            String OutCustName = outUserWalletAccount.getCustName();
            String InCustAcctId = inUserWalletAccount.getCustAcctId();
            String InThirdCustId = inUserWalletAccount.getCustAcctId();
            String InCustName = inUserWalletAccount.getCustName();
            String ThirdHtId = PinganUtils.generateThirdHtId();

            parmaKeyDict.put("TranFunc", "6034");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 功能标志
            parmaKeyDict.put("FuncFlag", "1");
            // 转出子账户
            parmaKeyDict.put("OutCustAcctId", OutCustAcctId);
            // 转出账户
            parmaKeyDict.put("OutThirdCustId", OutThirdCustId);
            // 转出账户名称
            parmaKeyDict.put("OutCustName", OutCustName);
            // 转入账户
            parmaKeyDict.put("InCustAcctId", InCustAcctId);
            // 转入账户名称
            parmaKeyDict.put("InThirdCustId", InThirdCustId);
            // 转入账户名称
            parmaKeyDict.put("InCustName", InCustName);
            // 转入金额
            parmaKeyDict.put("TranAmount", tranAmount);
            // 交易费用
            parmaKeyDict.put("TranFee", "0");
            // 交易类型
            parmaKeyDict.put("TranType", "01");
            parmaKeyDict.put("CcyCode", "RMB");
            // 交易单号
            parmaKeyDict.put("ThirdHtId", ThirdHtId);
//		parmaKeyDict.put("ThirdHtMsg", "10147221833939817430");
//		parmaKeyDict.put("Note", "10147221833939817430");
            String key = getSerialNoKey(outUserId);
            ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
            String SerialNo = valueops.get(key);
            parmaKeyDict.put("Reserve", SerialNo);  // 短信流水号
            parmaKeyDict.put("WebSign", messageCode);     // 短信验证码

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
                // 交易成功，记录交易日志
                TradeRecord tradeRecord = new TradeRecord();
                tradeRecord.setThirdHtId(ThirdHtId);
                tradeRecord.setOutCustAcctId(OutCustAcctId);
                tradeRecord.setOutThirdCustId(OutThirdCustId);
                tradeRecord.setOutCustName(OutCustName);
                tradeRecord.setInCustAcctId(InCustAcctId);
                tradeRecord.setInThirdCustId(InThirdCustId);
                tradeRecord.setInCustName(InCustName);
                tradeRecord.setTranAmount(tranAmount);
                tradeRecord.setStatus(TradeRecordStatus.COMMON_ACCOUNT.getCode());
                tradeRecord.setAddTime(new Date());
                tradeRecordService.create(tradeRecord);
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0050).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0050).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.OUT_ACCOUNT_TO_COMMON_ACCOUNT,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),outUserId);
        }
    }

    /**
     * 付款第一步：付款到担保账户
     * @param outUserId 用户id  付款用户id
     * @param inUserId 用户id 收款用户id
     * @param thirdHtId 交易唯一流水号，outToCommon的时候产生
     * @param messageCode 短信验证码
     */
    @RequestMapping(value = "commontoin")
    public Result commonToIn(Integer outUserId,Integer inUserId,String messageCode,String thirdHtId) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            logger.info("入参:" + "outUserId=" + outUserId + ",inUserId=" + inUserId + ",messageCode=" + messageCode
                    + ",thirdHtId=" + thirdHtId);
            // 参数校验
            if(outUserId == null || inUserId == null || StringUtils.isBlank(messageCode) || StringUtils.isBlank(thirdHtId)){
                // 必填参数为空
                return Result.error().msg(Error_code.ERROR_CODE_0045).data(new HashMap<>());
            }
            // 校验用户信息
            User outUser = userService.findById(outUserId);
            User inUser = userService.findById(inUserId);
            if(outUser == null){
                logger.info("outUser is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            if(inUser == null){
                logger.info("inUser is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            UserWalletAccount outUserWalletAccount = userWalletAccountService.findByUserId(outUserId);
            UserWalletAccount inUserWalletAccount = userWalletAccountService.findByUserId(inUserId);
            if(outUserWalletAccount == null){
                logger.info("outUserWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }
            if(inUserWalletAccount == null){
                logger.info("inUserWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            // 查询交易记录
            TradeRecord tradeRecord = tradeRecordService.findByThirdHtId(thirdHtId);
            if(tradeRecord == null){
                logger.info("tradeRecord is null");
                // 交易记录不存在
                return Result.error().msg(Error_code.ERROR_CODE_0049).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6034");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 功能标志
            parmaKeyDict.put("FuncFlag", "2");
            // 转出子账户
            parmaKeyDict.put("OutCustAcctId", outUserWalletAccount.getCustAcctId());
            // 转出账户
            parmaKeyDict.put("OutThirdCustId", outUserWalletAccount.getThirdCustId());
            // 转出账户名称
            parmaKeyDict.put("OutCustName", outUserWalletAccount.getCustName());
            // 转入账户
            parmaKeyDict.put("InCustAcctId", inUserWalletAccount.getCustAcctId());
            // 转入账户名称
            parmaKeyDict.put("InThirdCustId", inUserWalletAccount.getThirdCustId());
            // 转入账户名称
            parmaKeyDict.put("InCustName", inUserWalletAccount.getCustName());
            // 转入金额
            parmaKeyDict.put("TranAmount", tradeRecord.getTranAmount());
            // 交易费用
            parmaKeyDict.put("TranFee", "0");
            // 交易类型
            parmaKeyDict.put("TranType", "01");
            parmaKeyDict.put("CcyCode", "RMB");
            parmaKeyDict.put("ThirdHtId", thirdHtId);
//		parmaKeyDict.put("ThirdHtMsg", "10147221833939817430");
//		parmaKeyDict.put("Note", "10147221833939817430");
            String key = getSerialNoKey(outUserId);
            ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
            String SerialNo = valueops.get(key);
            parmaKeyDict.put("Reserve", SerialNo);  // 短信流水号
            parmaKeyDict.put("WebSign", messageCode);     // 短信验证码

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
                tradeRecord.setStatus(TradeRecordStatus.IN_ACCOUNT.getCode());
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0050).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0050).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.COMMON_ACCOUNT_TO_IN_ACCOUNT,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),outUserId);
        }
    }

    /**
     * 交易前发生验证码
     * @param outUserId 用户id  付款用户id
     * @param TranAmount 交易金额
     */
    @RequestMapping(value = "sendmsg4trade")
    public Result sendMsg4Trade(Integer outUserId,String TranAmount) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            logger.info("入参:" + "outUserId=" + outUserId + ",TranAmount=" + TranAmount);
            // 参数校验
            if(outUserId == null || StringUtils.isBlank(TranAmount)){
                // 必填参数为空
                return Result.error().msg(Error_code.ERROR_CODE_0045).data(new HashMap<>());
            }
            // 校验用户信息
            User outUser = userService.findById(outUserId);
            if(outUser == null){
                logger.info("outUser is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }

            UserWalletAccount outUserWalletAccount = userWalletAccountService.findByUserId(outUserId);
            if(outUserWalletAccount == null){
                logger.info("outUserWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6082");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", outUserWalletAccount.getThirdCustId());
            // 子账户账号
            parmaKeyDict.put("CustAcctId", outUserWalletAccount.getCustAcctId());
            // 交易类型（1=提现，2=交易 ）
            parmaKeyDict.put("TranType", "2");
            // 银行金额
            parmaKeyDict.put("TranAmount", TranAmount);

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
            String serialNo = (String) retKeyDict.get("SerialNo");
            if ("000000".equals(rspCode)) {
                // 验证码发生成功，验证码会发生到用户手机，短信流水号存入缓存
                ValueOperations<String, String> valueops = stringRedisTemplate.opsForValue();
                String key = getSerialNoKey(outUserId);
                valueops.set(key, serialNo);
                stringRedisTemplate.expire(key, 2, TimeUnit.MINUTES);
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0050).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0050).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.COMMON_ACCOUNT_TO_IN_ACCOUNT,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),outUserId);
        }
    }

    private String getSerialNoKey(Integer userId){
        return "SerialNo" + userId;
    }

    /**
     * 提现
     * @param userId 提现用户id
     * @param tranAmount 交易金额
     * @param acctIdName 提现账户名称（身份证上的姓名）
     */
    @RequestMapping(value = "withdraw")
    public Result withdraw(Integer userId,String tranAmount,String acctIdName) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            logger.info("入参:" + "userId=" + userId + ",tranAmount=" + tranAmount + ",acctIdName=" + acctIdName);
            // 参数校验
            if(userId == null || StringUtils.isBlank(tranAmount) || StringUtils.isBlank(acctIdName)){
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

            // 交易码
            parmaKeyDict.put("TranFunc", "6033");
            // 企业代码
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            // 请求流水号
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());
            // 交易网名称
            parmaKeyDict.put("TranWebName", "自由之家");
            // 子账户账号
            parmaKeyDict.put("CustAcctId", userWalletAccount.getCustAcctId());
            // 会员证件类型
            parmaKeyDict.put("IdType", "1");
            // 会员证件号码
            parmaKeyDict.put("IdCode", userWalletAccount.getIdCode());
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", userWalletAccount.getThirdCustId());
            // 子账户名称
            parmaKeyDict.put("CustName", userWalletAccount.getCustName());
            // 资金汇总账号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 提现账号
            parmaKeyDict.put("OutAcctId", userWalletAccount.getAcctId());
            // 提现账户名称
            parmaKeyDict.put("OutAcctIdName", acctIdName);
            // 币种
            parmaKeyDict.put("CcyCode", "RMB");
            // 申请提现金额
            parmaKeyDict.put("TranAmount",tranAmount);

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
                // 提现成功
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0051).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0051).data(new HashMap<>());
        } finally {
            pinganApiLogService.create(PinganApiEnum.WITHDRAW,
                    JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),userId);
        }
    }

    /**
     * 查询超级网银号信息
     */
    @RequestMapping(value = "superbank")
    public Result getSuperBankInfo() {
        try {
            List<SuperBankInfo> list = superBankInfoService.findAll();
            return Result.success().data(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }
}

