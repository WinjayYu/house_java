package com.ryel.zaja.pingan;

import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.TradeRecordStatus;
import com.ryel.zaja.entity.TradeRecord;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.entity.UserWalletAccount;
import com.ryel.zaja.service.TradeRecordService;
import com.ryel.zaja.service.UserService;
import com.ryel.zaja.service.UserWalletAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;

/**
 * 见证宝钱宝相关配置参数
 */
public class WalletConstant {

	protected final static Logger logger = LoggerFactory.getLogger(WalletConstant.class);
	/**
	 * 前置机IP
	 */
	public static final String SERVER_IP = "106.75.93.24";

	/**
	 * 前置机端口号
	 */
	public static final int SERVER_PORT = 7072;

	/**
	 * 见证宝资金汇总账号（监管账户）
	 */
	public static final String SUP_ACCT_ID = "11016544213188";

	/**
	 * 证书环境一般户
	 */
	public static final String COMMON_ACCT_ID = "11016611115009";

	/**
	 * 企业码
	 */
	public static final String QYDM = "3076";

	/**
	 * 快捷支付正式商户号
	 * 测试：2000311146
	 * 正式：2000766918
	 */
	public static final String QUICK_PAYMENT_ID = "2000766918";

	private static class SingletonHolder {
         private static final WalletConstant INSTANCE = new WalletConstant();
	}

    private WalletConstant (){

	}
    public static final WalletConstant getInstance() {
		return SingletonHolder.INSTANCE;
    }

	@Autowired
	private UserService userService;
	@Autowired
	private UserWalletAccountService userWalletAccountService;
	@Autowired
	private TradeRecordService tradeRecordService;


	/**
	 * 开通子账户
	 * @param userId
	 * @return
	 */
	public UserWalletAccount openAccount(Integer userId)
	{
		User user = userService.findById(userId);
		if(user == null){
			logger.error("user is null");
			return null;
		}
		HashMap<String,String> parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
		HashMap<String,String> retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
		try {

			parmaKeyDict.put("TranFunc", "6000"); // 交易码，此处以【6000】接口为例子
			parmaKeyDict.put("Qydm", WalletConstant.QYDM); // 企业代码
			parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号
			parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID); // 资金汇总账号
			parmaKeyDict.put("FuncFlag", "1"); // 功能标志1：开户 3销户
			parmaKeyDict.put("ThirdCustId", user.getId() +""); // 交易网会员代码
			parmaKeyDict.put("CustProperty", "00"); // 会员属性
			parmaKeyDict.put("NickName", user.getUsername()); // 会员昵称
			parmaKeyDict.put("MobilePhone", user.getMobile()); // 手机号码
			parmaKeyDict.put("Email", ""); // 邮箱
			parmaKeyDict.put("Reserve", "会员开户"); // 保留域

			ZJJZ_API_GW msg = new ZJJZ_API_GW();
			String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文

			msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
			String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文

			retKeyDict = msg.parsingTranMessageString(recvMessage);
			String custAcctId = (String) retKeyDict.get("CustAcctId");
			System.out.println("返回报文:=" + retKeyDict);

			String rspCode = (String) retKeyDict.get("RspCode");
			if ("000000".equals(rspCode)) {
				// 创建成功，写入数据库
				UserWalletAccount userWalletAccount = new UserWalletAccount();
				userWalletAccount.setUserId(user.getId());
				userWalletAccount.setThirdCustId(user.getId() + "");
				userWalletAccount.setCustAcctId(custAcctId);
				userWalletAccount.setMobilePhone(user.getMobile());
				userWalletAccount.setNickName(user.getUsername());
				userWalletAccountService.create(userWalletAccount);
				return userWalletAccount;
			} else {
				logger.error("见证宝错误信息", retKeyDict.get("RspMsg"));
				return null;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}


	/**
	 * 向子账户充钱
	 * @param userId 子账户信息
	 * @param amount
	 * @return ture 成功充值
	 */
	public  boolean rechargeMoney(Integer userId, String amount)
	{
		UserWalletAccount account = userWalletAccountService.findByUserId(userId);
		if(account == null){
			account = openAccount(userId);
		}

		HashMap<String,String> parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
		HashMap<String,String> retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
		try {

			parmaKeyDict.put("TranFunc", "6056");
			parmaKeyDict.put("Qydm", WalletConstant.QYDM);
			parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo()); // 请求流水号

			parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
			parmaKeyDict.put("ThirdCustId", account.getThirdCustId());
			parmaKeyDict.put("CustAcctId", account.getCustAcctId());
			parmaKeyDict.put("TranAmount", amount);
			parmaKeyDict.put("CcyCode", "RMB");
			parmaKeyDict.put("Note", "向会员子账户充钱");
			parmaKeyDict.put("Reserve", "1");


			ZJJZ_API_GW msg = new ZJJZ_API_GW();
			String tranMessage = msg.getTranMessage(parmaKeyDict);// 调用函数生成报文

			msg.SendTranMessage(tranMessage, WalletConstant.SERVER_IP, WalletConstant.SERVER_PORT, retKeyDict);
			String recvMessage = (String) retKeyDict.get("RecvMessage");// 银行返回的报文

			retKeyDict = msg.parsingTranMessageString(recvMessage);
			System.out.println("返回报文:=" + retKeyDict);

			String rspCode = (String) retKeyDict.get("RspCode");
			if ("000000".equals(rspCode)) {
				return true;
			} else {
				logger.error("见证宝错误信息", retKeyDict.get("RspMsg"));
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
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
	 * @param outUserId 资金出账户
	 * @param inUserId 资金入账户
	 * @param amount 金额
	 * @return
	 */
	public  boolean transactionMoney(String flag,Integer outUserId,Integer inUserId,String amount) {

		UserWalletAccount outUserWalletAccount = userWalletAccountService.findByUserId(outUserId);
		UserWalletAccount inUserWalletAccount = userWalletAccountService.findByUserId(inUserId);
		if(outUserWalletAccount == null){
			outUserWalletAccount = openAccount(outUserId);
		}
		if(inUserWalletAccount == null){
			inUserWalletAccount = openAccount(inUserId);
		}

		HashMap<String,String> parmaKeyDict = new HashMap<>();// 用于存放生成向银行请求报文的参数
		HashMap<String,String> retKeyDict = new HashMap<>();// 用于存放银行发送报文的参数
		try {
			String OutCustAcctId = outUserWalletAccount.getCustAcctId();
			String OutThirdCustId = outUserWalletAccount.getThirdCustId();
			String OutCustName = outUserWalletAccount.getNickName();

			String InCustAcctId = inUserWalletAccount.getCustAcctId();
			String InThirdCustId = inUserWalletAccount.getCustAcctId();
			String InCustName = inUserWalletAccount.getNickName();

			String ThirdHtId = PinganUtils.generateThirdHtId();

			parmaKeyDict.put("TranFunc", "6034");
			parmaKeyDict.put("Qydm", WalletConstant.QYDM);
			parmaKeyDict.put("ThirdLogNo", PinganUtils.generateThirdLogNo());
			parmaKeyDict.put("SupAcctId", WalletConstant.SUP_ACCT_ID);
			// 功能标志
			parmaKeyDict.put("FuncFlag", flag);
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
			parmaKeyDict.put("TranAmount", amount);
			// 交易费用
			parmaKeyDict.put("TranFee", "0");//平台手续费
			// 交易类型
			parmaKeyDict.put("TranType", "01");
			parmaKeyDict.put("CcyCode", "RMB");
			// 交易单号
			parmaKeyDict.put("ThirdHtId", ThirdHtId);

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
				// 交易成功，记录交易日志
				TradeRecord tradeRecord = new TradeRecord();
				tradeRecord.setThirdHtId(ThirdHtId);
				tradeRecord.setOutCustAcctId(OutCustAcctId);
				tradeRecord.setOutThirdCustId(OutThirdCustId);
				tradeRecord.setOutCustName(OutCustName);
				tradeRecord.setInCustAcctId(InCustAcctId);
				tradeRecord.setInThirdCustId(InThirdCustId);
				tradeRecord.setInCustName(InCustName);
				tradeRecord.setTranAmount(amount);
				tradeRecord.setStatus(TradeRecordStatus.COMMON_ACCOUNT.getCode());
				tradeRecord.setAddTime(new Date());
				tradeRecordService.create(tradeRecord);
				return true;
			} else {
				logger.error("见证宝错误信息", retKeyDict.get("RspMsg"));
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return false;
	}
}
