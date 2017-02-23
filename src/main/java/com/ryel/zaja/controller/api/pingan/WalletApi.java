package com.ryel.zaja.controller.api.pingan;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.PinganApiEnum;
import com.ryel.zaja.config.enums.TradeRecordStatus;
import com.ryel.zaja.entity.*;
import com.ryel.zaja.pingan.PinganUtils;
import com.ryel.zaja.pingan.ZJJZ_API_GW;
import com.ryel.zaja.service.*;
import com.ryel.zaja.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

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

            msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
            String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文


            retKeyDict = msg.parsingTranMessageString(recvMessage);
            System.out.println("返回报文:=" + retKeyDict);
            /**
             * 第三部分：解析银行返回的报文的实例
             */
            retKeyDict = msg.parsingTranMessageString(recvMessage);
            String custAcctId = (String) retKeyDict.get("CustAcctId");
            String rspCode = (String) retKeyDict.get("RspCode");
            String rspMsg = (String) retKeyDict.get("RspMsg");
            if ("000000".equals(rspCode) && StringUtils.isNotEmpty(custAcctId)) {
                return Result.success().data(new HashMap<>());
            } else if ("ERR114".equals(rspCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0041).data(new HashMap<>());
            } else {
                logger.error("创建见证宝账户报错" + rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }
    }


    /**
     * 查询所有的账户信息
     * @param page
     * @param type
     * @param custAcctId
     * @return
     */
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
            String rspMsg = (String) retKeyDict.get("RspMsg");
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
                return Result.success().data(data);
            } else {
                logger.error("查询所有的账户信息报错", rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
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
            User user = userService.findById(userId);


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
            String rspMsg = (String) retKeyDict.get("RspMsg");
            if ("000000".equals(rspCode)) {
                return Result.success().data(retKeyDict);
            } else {
                logger.error("向子账户充钱报错", rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }
    }

    /**
     * 子账户之间的交易
     * @param flag 建议 8-2 方案
     * 1：下单预支付 （付款方→担保）
    2：确认并付款（担保→收款方）
    3：退款（担保→付款方）
    4：支付到平台（担保→平台，平台退回到银行卡）
    6：直接支付（会员A→会员B）
    7：支付到平台（会员→平台）
    8：清分支付（清分→会员→担保）
    9：直接支付T+0（会员A→会员B）
     * @param amount 金额
     * @return
     */
    @RequestMapping(value = "transactionMoney")
    public Result transactionMoney(String flag,String outCustAcctId,String inCustAcctId,String outThirdCustId,String inThirdCustId,String outCustName,String inCustName,String amount) {

        HashMap<String,String> parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
        HashMap<String,String> retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
        try {

            String ThirdHtId = PinganUtils.generateThirdHtId();

            parmaKeyDict.put("TranFunc", "6034");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM);
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            // 功能标志
            parmaKeyDict.put("FuncFlag", flag);
            // 转出子账户
            parmaKeyDict.put("OutCustAcctId", outCustAcctId);
            // 转出账户
            parmaKeyDict.put("OutThirdCustId", outThirdCustId);
            // 转出账户名称
            parmaKeyDict.put("OutCustName", outCustName);
            // 转入账户
            parmaKeyDict.put("InCustAcctId", inCustAcctId);
            // 转入账户名称
            parmaKeyDict.put("InThirdCustId", inThirdCustId);
            // 转入账户名称
            parmaKeyDict.put("InCustName", inCustName);
            // 转入金额
            parmaKeyDict.put("TranAmount", amount);
            // 交易费用
            parmaKeyDict.put("TranFee", "0");//平台手续费
            // 交易类型
            parmaKeyDict.put("TranType", "01");
            parmaKeyDict.put("CcyCode", "RMB");
            // 交易单号
            parmaKeyDict.put("ThirdHtId", ThirdHtId);
            parmaKeyDict.put("ThirdHtMsg", outCustAcctId + "——" + inCustAcctId + "资金进入担保账户");
            parmaKeyDict.put("Note", "资金进入担保账户");

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
            String rspMsg = (String) retKeyDict.get("RspMsg");
            if ("000000".equals(rspCode)) {
                return Result.success().data(retKeyDict);
            } else {
                logger.error("子账户之间的交易信息", rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }

    }

    /**
     * 会员绑定信息查询
     */
    @RequestMapping(value = "querybindinfo")
    public Result queryBindInfo(Integer page, String type, String custAcctId) {
        HashMap parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
        try {

            parmaKeyDict.put("TranFunc", "6098"); // 交易码，此处以【6000】接口为例子
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号

            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
            parmaKeyDict.put("SelectFlag", type); // 1：全部会员 2：单个会员
            parmaKeyDict.put("PageNum", page + "");

            if(type.equals("2")) {
                parmaKeyDict.put("CustAcctId", custAcctId); // 交易网会员代码
            }

            parmaKeyDict.put("Reserve", "会员绑定信息查询"); // 保留域


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
            if (!"000000".equals(rspCode)) {
                logger.error("见证宝错误信息", retKeyDict.get("RspMsg"));
                return Result.error().msg(rspCode).data(new HashMap<>());
            }

            String TotalCount = (String) retKeyDict.get("TotalCount");
            String Reserve = (String) retKeyDict.get("Reserve");

            String arrayContent = (String) retKeyDict.get("ArrayContent"); //ArrayContent为固定名称。
            String[] array = arrayContent.split("&");

            List list = new ArrayList();

            for (int i = 0; i < array.length; i = i + 12) {
                Map map = new HashMap<>();
                map.put("SupAcctId", array[i]);
                map.put("CustAcctId", array[i + 1]);
                map.put("ThirdCustId", array[i + 2]);
                map.put("CustName", array[i + 3]);
                map.put("IdType", array[i + 4]);
                map.put("IdType", array[i + 5]);
                map.put("AcctId", array[i + 6]);
                map.put("BankType", array[i + 7]);
                map.put("BankName", array[i + 8]);
                map.put("BankCode", array[i + 9]);
                map.put("SBankCode", array[i + 10]);
                map.put("MobilePhone", array[i + 11]);
                list.add(map);
            }

            Map data = new HashMap<>();
            data.put("list", list);

            return Result.success().data(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }
    }


    /**
     * =======================================================================================================================
     * 经纪人使用接口如下
     */


    /**
     * 查询见证宝余额信息
     *
     * @param agentId 用户id
     */
    @RequestMapping(value = "walletinfo")
    public Result walletinfo(Integer agentId) {
        HashMap<String,String> parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
        HashMap retKeyDict = new HashMap();// 用于存放银行发送报文的参数
        try {

            parmaKeyDict.put("TranFunc", "6037");
            parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
            parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
            parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
            parmaKeyDict.put("ThirdCustId", agentId + "");
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
            String rspMsg = (String) retKeyDict.get("RspMsg");
            if ("000000".equals(rspCode)) {
                Map data = new HashMap<>();
                Map response = new HashMap<>();
                response.put("frozenAmount", retKeyDict.get("TotalFreezeAmount"));
                response.put("takeAmount", retKeyDict.get("TotalAmount"));
                response.put("sendAmount",retKeyDict.get("TotalBalance"));
                data.put("wallet", response);
                return Result.success().data(data);
            } else {
                logger.error("查询见证宝余额信息报错", rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }finally {
//            pinganApiLogService.create(PinganApiEnum.GET_WALLET_BALANCE_INFO, JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),parmaKeyDict.get("ThirdLogNo"));
        }
    }

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
     * String aCcId, String bankName, String sBankCode, String bankCode, String sBankName
     */
    @RequestMapping(value = "sendnote")
    public Result sendOpenCardNote(Integer agentId, String idCode, UserWalletAccount account) {
        User user = userService.findById(agentId);

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
            String rspMsg = (String) retKeyDict.get("RspMsg");

            if ("000000".equals(rspCode)) {
                // 发送验证码
                return Result.success().data(new HashMap<>());
            } else if("ERR134".equals(rspCode)) {
                return Result.error().msg(Error_code.ERROR_CODE_0045).data(new HashMap<>());
            }else
            {
                logger.error("开通提现银行卡报错" + rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }finally {
            pinganApiLogService.create(PinganApiEnum.BIND_WITHDRAW_CARD_SEND_MSG, JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),parmaKeyDict.get("ThirdLogNo"));
        }

    }

    /**
     * 绑定提现银行卡
     * @param agentId
     * @param account 钱包信息
     * @param messageCode 短信验证码
     * @return
     *  String idCode, String aCcId, String bankName, String bankCode,String sBankCode,String sBankName
     */
    @RequestMapping(value = "tiebank")
    public Result tieBankCard(Integer agentId, UserWalletAccount account, String messageCode) {
        User user = userService.findById(agentId);

        HashMap<String,String> parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
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
            String rspMsg = (String) retKeyDict.get("RspMsg");
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
                logger.error("绑定提现银行卡报错" + rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }finally {
            pinganApiLogService.create(PinganApiEnum.BIND_WITHDRAW_CARD, JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),parmaKeyDict.get("ThirdLogNo"));
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

        HashMap<String,String> parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
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
            String rspMsg = (String) retKeyDict.get("RspMsg");
            if ("000000".equals(rspCode)) {
                UserWalletAccount userWalletAccount = userWalletAccountService.findByACcId(accId);
                userWalletAccount.setStatus("20");
                userWalletAccountService.update(userWalletAccount);
                // 解除成功
                return Result.success().data(new HashMap<>());
            } else {
                logger.error("解绑提现银行卡报错" + rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }finally {
            pinganApiLogService.create(PinganApiEnum.REMOVE_WITHDRAW_CARD, JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),parmaKeyDict.get("ThirdLogNo"));
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

        HashMap<String,String> parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
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
            String rspMsg = (String) retKeyDict.get("RspMsg");
            String serialNo = (String) retKeyDict.get("SerialNo");
            if ("000000".equals(rspCode)) {
                //返回短信指令号
                Map data = new HashMap();
                data.put("serialNo",serialNo);
                return Result.success().data(data);
            } else {
                logger.error("提现短信验证报错" + rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }finally {
            pinganApiLogService.create(PinganApiEnum.WITHDRAW_MONEY_SEND_MSG, JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),parmaKeyDict.get("ThirdLogNo"));
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

        HashMap<String,String> parmaKeyDict = new HashMap();// 用于存放生成向银行请求报文的参数
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
            String rspMsg = (String) retKeyDict.get("RspMsg");
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
                logger.error("提现报错" + rspMsg);
                return Result.error().msg(rspMsg).data(new HashMap<>());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0044).data(new HashMap<>());
        }finally {
            pinganApiLogService.create(PinganApiEnum.WITHDRAW, JsonUtil.obj2Json(parmaKeyDict),JsonUtil.obj2Json(retKeyDict),parmaKeyDict.get("ThirdLogNo"));
        }

    }
}

