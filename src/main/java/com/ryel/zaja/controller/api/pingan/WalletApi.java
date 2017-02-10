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
import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping(value = "/api/pingan/wallet", produces = "application/json; charset=UTF-8")
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
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SuperBankInfoService superBankInfoService;
    @Autowired
    private WalletConstant wallet;
    @Autowired
    private OutCashFlowService outCashFlowService;


    /**
     * 创建见证宝账户
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "createaccount")
    public Result createAccount(String userId, Integer type) {
        HashMap parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
        try {
            // 校验用户信息
//            if(userId == null){
//                logger.info("userId:" + userId);
//                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
//            }
//            User user = userService.findById(userId);
//            if(user == null){
//                logger.info("user is null");
//                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
//            }
//            String mobile = user.getMobile();
//            String nickname = user.getUsername();
//            if(StringUtils.isBlank(mobile) || mobile.length() != 11 || StringUtils.isBlank(nickname)){
//                logger.info("user:" + JsonUtil.obj2Json(user));
//                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
//            }

            String mobile = "";
            String nickname = "";

            parmaKeyDict.put("TranFunc", "6000"); // 交易码，此处以【6000】接口为例子
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
            parmaKeyDict.put("FuncFlag", type + ""); // 功能标志1：开户 3销户
            parmaKeyDict.put("ThirdCustId", userId + ""); // 交易网会员代码
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
//                UserWalletAccount userWalletAccount = new UserWalletAccount();
//                userWalletAccount.setUserId(userId);
//                userWalletAccount.setThirdCustId(userId.toString());
//                userWalletAccount.setCustAcctId(custAcctId);
//                userWalletAccount.setMobilePhone(mobile);
//                userWalletAccount.setNickName(nickname);
//                userWalletAccountService.create(userWalletAccount);
                return Result.success().data(new HashMap<>());
            } else if ("ERR114".equals(rspCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0041).data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
        }
    }

    @RequestMapping(value = "removeaccount")
    public Result removeAccount(Integer userId) {
        HashMap parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
        try {
            // 校验用户信息
            if (userId == null) {
                logger.info("userId:" + userId);
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            User user = userService.findById(userId);
            if (user == null) {
                logger.info("user is null");
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }
            String mobile = user.getMobile();
            String nickname = user.getUsername();
            if (StringUtils.isBlank(mobile) || mobile.length() != 11 || StringUtils.isBlank(nickname)) {
                logger.info("user:" + JsonUtil.obj2Json(user));
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6000"); // 交易码，此处以【6000】接口为例子
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
            parmaKeyDict.put("FuncFlag", "3"); // 功能标志1：开户 3销户
            parmaKeyDict.put("ThirdCustId", user.getId() + ""); // 交易网会员代码
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
                //移除成功
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
        }
    }

    @RequestMapping(value = "allacount")
    public Result allAcount(Integer page, String type, String custAcctId) {
        HashMap parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
        try {


            parmaKeyDict.put("TranFunc", "6010"); // 交易码，此处以【6000】接口为例子
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
            parmaKeyDict.put("SelectFlag", type); // 1：全部 2：普通会员子账号 3：功能子账号
            parmaKeyDict.put("PageNum", page + ""); // 交易网会员代码
            parmaKeyDict.put("CustAcctId", custAcctId); // 交易网会员代码
            parmaKeyDict.put("Reserve", "所有的子账户查询"); // 保留域


            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文


            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文


            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");

            String TotalCount = (String) retKeyDict.get("TotalCount");
            String Reserve = (String) retKeyDict.get("Reserve");

            String arrayContent = (String) retKeyDict.get("ArrayContent"); //ArrayContent为固定名称。
            String[] array = arrayContent.split("&");

            List list = new ArrayList();

            for (int i = 0; i < array.length; i = i + 7) {
                Map map = new HashMap<>();
                map.put("CustAcctId", array[i]);
                map.put("CustType", array[i + 1]);
                map.put("ThirdCustId", array[i + 2]);
                map.put("CustName", array[i + 3]);
                map.put("TotalBalance", array[i + 4]);
                map.put("TotalTranOutAmount", array[i + 5]);
                map.put("TranDate", array[i + 6]);
                list.add(map);
            }

            Map data = new HashMap<>();
            data.put("list", list);


            if ("000000".equals(rspCode)) {
                //移除成功
                return Result.success().data(data);
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0040).data(new HashMap<>());
        }
    }

    /**
     * 移除此商户号的账户信息
     *
     * @param userId
     * @param acctId
     * @return
     */
    @RequestMapping(value = "removeAccount")
    public Result removeAccount(Integer userId, String acctId) {

        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数

        try {


            if (userId == null) {
                logger.info("userId:" + userId);
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }


            parmaKeyDict.put("TranFunc", "6065");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("FuncFlag", "1"); // 功能标志1：移除
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            parmaKeyDict.put("ThirdCustId", userId + "");
            parmaKeyDict.put("AcctId", acctId);
            parmaKeyDict.put("Reserve", "removeAccount");

            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文

            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文

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
        }
    }

    /**
     * 查询见证宝余额信息
     *
     * @param userId 用户id
     */
    @RequestMapping(value = "walletinfo")
    public Result walletinfo(Integer userId) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            // 校验用户信息
            if (userId == null) {
                logger.info("userId:" + userId);
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            User user = userService.findById(userId);
            if (user == null) {
                logger.info("userWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6037");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            parmaKeyDict.put("ThirdCustId", userId + "");
            parmaKeyDict.put("Reserve", "1");

            System.out.println("请求报文==============" + parmaKeyDict);

            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文


            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文

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
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                Map data = new HashMap<>();
                Map response = new HashMap<>();
                response.put("frozenAmount", retKeyDict.get("TotalFreezeAmount"));
                response.put("takeAmount", retKeyDict.get("TotalAmount"));
                data.put("wallet", response);
                return Result.success().data(data);
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }
    }


    /**
     * 向会员子账户充钱
     *
     * @param userId
     */
    @RequestMapping(value = "rechargecccount")
    public Result rechargeAccount(Integer userId, String amount) {
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {
            // 校验用户信息
            if (userId == null) {
                logger.info("userId:" + userId);
                return Result.error().msg(Error_code.ERROR_CODE_0023).data(new HashMap<>());
            }
            User user = userService.findById(userId);
            if (user == null) {
                logger.info("userWalletAccount is null");
                return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
            }

            parmaKeyDict.put("TranFunc", "6056");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            parmaKeyDict.put("ThirdCustId", userId + "");
            parmaKeyDict.put("CustAcctId", user.getCustAcctId());
            parmaKeyDict.put("TranAmount", amount);
            parmaKeyDict.put("CcyCode", "RMB");
            parmaKeyDict.put("Note", "向会员子账户充钱");
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
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                return Result.success().data(retKeyDict);
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
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
                String TotalCount = (String) retKeyDict.get("TotalCount");
                Integer iCount = Integer.valueOf(TotalCount);
                String ArrayContent = (String) retKeyDict.get("ArrayContent"); //ArrayContent为固定名称。
                String[] array = ArrayContent.split("&");

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

                for (i = 0, j = 0; i < 35; i = i + 7, j++) {
                    CustAcctId[j] = array[i];
                    CustType[j] = array[i + 1];
                    ThirdCustId[j] = array[i + 2];
                    CustName[j] = array[i + 3];
                    TotalBalance[j] = array[i + 4];
                    TotalTranOutAmount[j] = array[i + 5];
                    TranDate[j] = array[i + 6];
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






    private String getSerialNoKey(Integer userId) {
        return "SerialNo" + userId;
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

    /**
     * =======================================================================================================================
     * 经纪人使用接口如下
     */


    /**
     * 获取个人已开通银行列表
     * @param agentId
     * @return
     */
    @RequestMapping(value = "bankcards")
    public Result bankCardList(Integer agentId)
    {
        try {
            List<UserWalletAccount> list = userWalletAccountService.findByUserId(agentId);
            Map data = new HashMap();
            data.put("list",list);
            return  Result.success().data(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 经纪人开通提现银行卡
     *
     * @param agentId
     * @param idCode   身份证
     * @param account  开卡信息
     * @return
     * String accId, String bankName, String sBankCode, String bankCode, String sBankName
     */
    @RequestMapping(value = "sendnote")
    public Result sendOpenCardNote(Integer agentId, String idCode, UserWalletAccount account) {
        User user = userService.findById(agentId);
        if (user == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
        HashMap<String,String> parmaKeyDict = new HashMap<String,String>();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数

        try {
            parmaKeyDict.put("TranFunc", "6066");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", user.getId() + "");
            // 子账户账号
            parmaKeyDict.put("CustAcctId", user.getCustAcctId());
            // 子账户名称
            parmaKeyDict.put("CustName", user.getUsername());
            // 会员证件类型
            parmaKeyDict.put("IdType", "1");
            // 身份证
            parmaKeyDict.put("IdCode", idCode);
            //银行卡号
            parmaKeyDict.put("AcctId", account.getaCctId());

            parmaKeyDict.put("MobilePhone", user.getMobile());

            if(account.getBankName() !=null)
            {
                parmaKeyDict.put("BankName",account.getBankName());
            }else{
                parmaKeyDict.put("BankName",account.getsBankName());
            }

            //大于5万的大小额号
            if (account.getBankCode() != null) {
                parmaKeyDict.put("BankCode", account.getBankCode());
            }
            //小于等于5万的超级网银号
            if (account.getsBankCode() != null) {
                parmaKeyDict.put("SBankCode", account.getsBankCode() );
            }

            if (parmaKeyDict.get("BankName").contains("平安银行")) {
                //本行
                parmaKeyDict.put("BankType", "1");
            } else {
                parmaKeyDict.put("BankType", "2");
            }
            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文


            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文


            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                // 发送验证码
                return Result.success().data(new HashMap<>());
            } else if("ERR134".equals(rspCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0052).data(new HashMap<>());
            }else
            {
                return Result.error().msg(Error_code.ERROR_CODE_0047).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }

    /**
     * 绑定提现银行卡
     * @param agentId
     * @param account 钱包信息
     * @param messageCode 短信验证码
     * @return
     *  String idCode, String accId, String bankName, String bankCode,String sBankCode,String sBankName
     */
    @RequestMapping(value = "tiebank")
    public Result tieBankCard(Integer agentId, UserWalletAccount account, String messageCode) {
        User user = userService.findById(agentId);
        if (user == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数

        try {
            parmaKeyDict.put("TranFunc", "6067");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", user.getId()+"");
            // 子账户账号
            parmaKeyDict.put("CustAcctId", user.getCustAcctId());
            // 子账户名称
            parmaKeyDict.put("CustName", user.getUsername());

            //银行卡号
            parmaKeyDict.put("AcctId", account.getaCctId());

            parmaKeyDict.put("MessageCode", messageCode);

            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文


            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文


            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                // 绑定成功
                account.setStatus("10");
                account.setUserId(agentId);

                if(account.getBankName().contains("平安银行") || account.getsBankName().contains("平安银行"))
                {
                    account.setBankType("10");
                }else{
                    account.setBankType("20");
                }
                userWalletAccountService.create(account);
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0051).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }

    /**
     * 解绑提现账户
     * @param agentId
     * @param accId 银行卡号
     * @return
     */
    @RequestMapping(value = "removebank")
    public Result removeBankCard(Integer agentId, String accId) {
        User user = userService.findById(agentId);
        if (user == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数

        try {
            parmaKeyDict.put("TranFunc", "6065");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);

            parmaKeyDict.put("FuncFlag", "1");
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", user.getId()+"");
            // 子账户账号
            parmaKeyDict.put("CustAcctId", user.getCustAcctId());
            // 子账户名称
            parmaKeyDict.put("CustName", user.getUsername());

            //银行卡号
            parmaKeyDict.put("AcctId", accId);


            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文


            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文


            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");
            if ("000000".equals(rspCode)) {
                UserWalletAccount userWalletAccount = userWalletAccountService.findByACcId(accId);
                userWalletAccount.setStatus("20");
                userWalletAccountService.update(userWalletAccount);
                // 解除成功
                return Result.success().data(new HashMap<>());
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0051).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }

    /**
     * 提现申请的短信验证信息
     * @param agentId
     * @param accId
     * @param amount
     * @return
     */
    @RequestMapping(value = "withdrawnnote")
    public Result withdrawnNote(Integer agentId, String accId, String amount) {
        User user = userService.findById(agentId);
        if (user == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数

        try {
            parmaKeyDict.put("TranFunc", "6082");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);

            parmaKeyDict.put("TranType", "1");// 1 提现 2 支付
            parmaKeyDict.put("TranAmount", amount);// 1 提现 2 支付
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", user.getId()+"");
            // 子账户账号
            parmaKeyDict.put("CustAcctId", user.getCustAcctId());
            // 子账户名称
            parmaKeyDict.put("CustName", user.getUsername());

            //银行卡号
            parmaKeyDict.put("AcctId", accId);


            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文


            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文


            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");
            String serialNo = (String) retKeyDict.get("SerialNo");
            if ("000000".equals(rspCode)) {
                //返回短信指令号
                Map data = new HashMap();
                data.put("serialNo",serialNo);
                return Result.success().data(data);
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0051).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }

    /**
     * 提现
     * @param agentId
     * @param accId 卡号
     * @param amount
     * @param seriaNo 短信指令号
     * @param code 短信验证码
     * @return
     */
    @RequestMapping(value = "withdrawnmoney")
    public Result withdrawnMoney(Integer agentId, String accId, String amount,String seriaNo, String code) {
        User user = userService.findById(agentId);
        if (user == null) {
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
        HashMap parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数

        try {
            parmaKeyDict.put("TranFunc", "6085");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);

            parmaKeyDict.put("TranType", "1");// 1 提现 2 支付
            parmaKeyDict.put("TranAmount", amount);// 1 提现 2 支付
            // 交易网会员代码
            parmaKeyDict.put("ThirdCustId", user.getId()+"");
            // 子账户账号
            parmaKeyDict.put("CustAcctId", user.getCustAcctId());
            // 子账户名称
            parmaKeyDict.put("CustName", user.getUsername());

            // 提现账号
            parmaKeyDict.put("OutAcctId", accId);
            // 提现账户名称 银行卡户名，必须与子账户名称一致
            parmaKeyDict.put("OutAcctIdName", user.getUsername());

            // 提现金额
            parmaKeyDict.put("TranAmount", amount);

            //币种
            parmaKeyDict.put("CcyCode", "RMB");

            Double money = Double.valueOf(amount);
            // 提现手续费
            parmaKeyDict.put("HandFee", money * 0.006 + "");

            // 提现账号
            parmaKeyDict.put("SerialNo", seriaNo);
            // 提现账户名称 银行卡户名，必须与子账户名称一致
            parmaKeyDict.put("MessageCode", code);


            ZJJZ_API_GW msg = new ZJJZ_API_GW();
            String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文


            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文


            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String rspCode = (String) retKeyDict.get("RspCode");
            String frontLogNo = (String) retKeyDict.get("FrontLogNo");
            if ("000000".equals(rspCode)) {
                OutCashFlow cash = new OutCashFlow();
                cash.setAccId(accId);
                cash.setUserId(user.getId()+"");
                cash.setCustAccId(user.getCustAcctId());
                cash.setAmount(amount);
                cash.setUsername(user.getUsername());
                cash.setFee(money * 0.006+"");
                cash.setFrontLogNo(frontLogNo);
                outCashFlowService.create(cash);
                Map data = new HashMap();
                data.put("handFee",String.format("%.1f", money * 0.006));
                return Result.success().data(data);
            } else {
                return Result.error().msg(Error_code.ERROR_CODE_0051).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }
}

