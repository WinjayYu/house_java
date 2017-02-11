package com.ryel.zaja.pingan;

import com.ryel.zaja.controller.api.pingan.WalletConstant;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class ZJJZ_API_GW {
	/* 生成请求银行的完整报文 */
	public String getTranMessage(HashMap parmaKeyDict) {
		// String netType=(String)parmaKeyDict.get("NetType");//通讯类型：ZX：专线；GW：公网
		// String
		// hServType=(String)parmaKeyDict.get("ServType");//服务类型：01:请求02:应答

		byte[] byteMessageBody;
		String tranMessage = "";
		String tranMessageBody = "";

		/* 组业务报文体 */
		tranMessageBody = getTranMessageBody(parmaKeyDict);

		try {
			byteMessageBody = tranMessageBody.getBytes("gbk");// 编码
		} catch (UnsupportedEncodingException ex) {
			return ex.toString();
		}

		/* 组公网业务报文头 */
		int iLength = byteMessageBody.length;
		String hLength = String.format("%08d", iLength);
		String tranMessageHead = getTranMessageHead(hLength, parmaKeyDict);

		/* 组公网通讯报文头 */
		int iNetLength = iLength + 122;
		String hNetLength = String.format("%010d", iNetLength);
		String tranMessageNetHead = getTranMessageNetHead(hNetLength,
				parmaKeyDict);

		/* 组完整请求报文 */
		tranMessage = tranMessageNetHead + tranMessageHead + tranMessageBody;

		return tranMessage;
	}

	public String getTranMessage_ZX(HashMap parmaKeyDict) {
		// String netType=(String)parmaKeyDict.get("NetType");//通讯类型：ZX：专线；GW：公网
		// String
		// hServType=(String)parmaKeyDict.get("ServType");//服务类型：01:请求02:应答

		byte[] byteMessageBody;
		String tranMessage = "";
		String tranMessageBody = "";

		/* 组业务报文体 */
		tranMessageBody = getTranMessageBody(parmaKeyDict);

		try {
			byteMessageBody = tranMessageBody.getBytes("gbk");// 编码
		} catch (UnsupportedEncodingException ex) {
			return ex.toString();
		}

		/* 组公网业务报文头 */
		int iLength = byteMessageBody.length;
		String hLength = String.format("%08d", iLength);
		String tranMessageHead = getTranMessageHead(hLength, parmaKeyDict);

		/* 组完整请求报文 */
		tranMessage = tranMessageHead + tranMessageBody;

		return tranMessage;
	}

	/* 发送报文，并接收银行返回 */
	public void SendTranMessage(String tranMessage, String ServerIPAddress,
			int ServerPort, HashMap retKeyDict) {
		try {

			Socket s = new Socket(ServerIPAddress, ServerPort);
			s.setSendBufferSize(4096);
			s.setTcpNoDelay(true);
			s.setSoTimeout(90000);
			s.setKeepAlive(true);
			// Socket s = new Socket(InetAddress.getByName(null),port);
			OutputStream os = s.getOutputStream();
			InputStream is = s.getInputStream();

			os.write(tranMessage.getBytes("gbk"));
			os.flush();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			int len = -1;
			while ((len = is.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			byte[] byteContent = bos.toByteArray();
			String recvMessage = new String(byteContent, "gbk");
			recvMessage = recvMessage + '\0';// 修复使用spilt方法时的数组越界错误
			retKeyDict.put("RecvMessage", recvMessage);

			os.close();
			is.close();

		} catch (IOException ex) {
			System.out.println(ex.toString());
		}

	}

	/* 解析接收银行的报文，入参String类型 */
	public HashMap parsingTranMessageString(String TranMessage) {
		HashMap retKeyDict = new HashMap();
		int i;
		byte[] bNetHead = new byte[222];
		byte[] bTranFunc = new byte[226];
		byte[] bRspCode = new byte[93];
		byte[] bRspMsg = new byte[193];
		byte[] bHeadMsg = new byte[344];

		/*
		 * 转换为GBK格式 try{ byte[] byteRetMessage =
		 * TranMessage.getBytes("gbk");//编码 String sRevMsg= new
		 * String(byteRetMessage,"gbk"); retKeyDict.put("RevMsg_GBK",sRevMsg);
		 * }catch(UnsupportedEncodingException ex) {
		 * System.out.println(ex.toString()); }
		 * 
		 * String revMsg_GBK=(String)retKeyDict.get("RevMsg_GBK");
		 */

		/* 获取返回码 */
		try {
			byte[] byteRetMessage = TranMessage.getBytes("gbk");// 编码
			for (i = 0; i < 93; i++) {
				bRspCode[i] = byteRetMessage[i];
			}
			String sRspCode = new String(bRspCode, "gbk");
			sRspCode = sRspCode.substring(87);
			retKeyDict.put("RspCode", sRspCode);
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.toString());
		}

		/* 获取返回信息 */
		try {
			byte[] byteRetMessage = TranMessage.getBytes("gbk");// 编码
			for (i = 0; i < 193; i++) {
				bRspMsg[i] = byteRetMessage[i];
			}
			String sRspMsg = new String(bRspMsg, "gbk");
			sRspMsg = sRspMsg.substring(93);
			retKeyDict.put("RspMsgBak", sRspMsg);
			sRspMsg = sRspMsg.trim();
			retKeyDict.put("RspMsg", sRspMsg);
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.toString());
		}

		String strCode = (String) retKeyDict.get("RspCode");

		if ("000000".equals(strCode)) {
			/* 获取交易码 */
			try {
				byte[] byteRetMessage = TranMessage.getBytes("gbk");// 编码
				for (i = 0; i < 226; i++) {
					bTranFunc[i] = byteRetMessage[i];
				}
				String sTranFunc = new String(bTranFunc, "gbk");

				for (i = 0; i < 222; i++) {
					bNetHead[i] = byteRetMessage[i];
				}
				String sNetHead = new String(bNetHead, "gbk");

				// String strRspMsg=(String)retKeyDict.get("RspMsg");
				int iTranLength = sNetHead.length();
				sTranFunc = sTranFunc.substring(iTranLength);
				// System.out.println(sTranFunc);
				retKeyDict.put("TranFunc", sTranFunc);
			} catch (UnsupportedEncodingException ex) {
				System.out.println(ex.toString());
			}

			// String strFunc=(String)retKeyDict.get("TranFunc");
			// System.out.println(strFunc);
			/* 获取返回报文体 */
			try {
				byte[] byteRetMessage = TranMessage.getBytes("gbk");// 编码
				String sBodyMsg = new String(byteRetMessage, "gbk");// 解码
				for (i = 0; i < 344; i++) {
					bHeadMsg[i] = byteRetMessage[i];
				}
				String sHeadMsg = new String(bHeadMsg, "gbk");
				// String strRspMsg=(String)retKeyDict.get("RspMsg");
				int iLength = sHeadMsg.length();
				sBodyMsg = sBodyMsg.substring(iLength);
				retKeyDict.put("BodyMsg", sBodyMsg);
			} catch (UnsupportedEncodingException ex) {
				System.out.println(ex.toString());
			}

			/* 解析报文体 */
			spiltMessage(retKeyDict);

		}

		return retKeyDict;

	}

	public String getSignMessage(HashMap parmaKeyDict) {
		String signMessageBody = "";
		int hTranFunc = Integer.parseInt((String) parmaKeyDict.get("TranFunc"));
		switch (hTranFunc) {
		case 6005:
			signMessageBody = getSignMessageBody_6005(parmaKeyDict);
			break;
		case 6006:
			signMessageBody = getSignMessageBody_6006(parmaKeyDict);
			break;
		default:
			signMessageBody = "交易码未配置，请联系银行技术人员更新接口API";
		}
		return signMessageBody;
	}

	/* 生成请求银行报文的报文头 */
	private String getTranMessageHead(String hLength, HashMap parmaKeyDict) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

		String hServType = "01";
		String hMacCode = "                ";
		String hTrandateTime = df.format(new Date());
		String hRspCode = "999999";
		String hRspMsg = "                                          ";
		String hConFlag = "0";
		String hCounterId = "PA001";

		String hTranFunc = (String) parmaKeyDict.get("TranFunc");
		String hThirdLogNo = (String) parmaKeyDict.get("ThirdLogNo");
		String hQydm = (String) parmaKeyDict.get("Qydm");

		String tranMessageHead = hTranFunc + hServType + hMacCode
				+ hTrandateTime + hRspCode + hRspMsg + hConFlag + hLength
				+ hCounterId + hThirdLogNo + hQydm;

		return tranMessageHead;
	}

	private String getTranMessageNetHead(String hLength, HashMap parmaKeyDict) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

		String netHeadPart1 = "A001130101";
		String netHeadPart2 = "                ";// 16个空格
		String hTradeCode = "000000";
		String hServType = "01";
		// String hMacCode="                ";
		String hTrandateTime = df.format(new Date());
		String hRspCode = "999999";
		String hRspMsg = "                                                                                                    ";// 100个空格
		String hConFlag = "0";
		String hCounterId = "PA001";
		String hTimes = "000";
		String hSignFlag = "0";
		String hSignPacketType = "0";
		String netHeadPart3 = "            ";// 12个空格
		String netHeadPart4 = "00000000000";

		// String hTranFunc=(String)parmaKeyDict.get("TranFunc");
		String hThirdLogNo = (String) parmaKeyDict.get("ThirdLogNo");
		String hQydm = (String) parmaKeyDict.get("Qydm");

		String tranMessageNetHead = netHeadPart1 + hQydm + netHeadPart2
				+ hLength + hTradeCode + hCounterId + hServType + hTrandateTime
				+ hThirdLogNo + hRspCode + hRspMsg + hConFlag + hTimes
				+ hSignFlag + hSignPacketType + netHeadPart3 + netHeadPart4;

		return tranMessageNetHead;
	}

	/* 根据交易码调用不同的交易请求报文生成方法 */
	private String getTranMessageBody(HashMap parmaKeyDict) {
		String tranMessageBody = "";
		int hTranFunc = Integer.parseInt((String) parmaKeyDict.get("TranFunc"));

		switch (hTranFunc) {
		case 6000:
			tranMessageBody = getTranMessageBody_6000(parmaKeyDict);
			break;
		case 6005:
			tranMessageBody = getTranMessageBody_6033(parmaKeyDict);
			break;
		case 6006:
			tranMessageBody = getTranMessageBody_6034(parmaKeyDict);
			break;
		case 6007:
			tranMessageBody = getTranMessageBody_6007(parmaKeyDict);
			break;
		case 6008:
			tranMessageBody = getTranMessageBody_6008(parmaKeyDict);
			break;
		case 6010:
			tranMessageBody = getTranMessageBody_6010(parmaKeyDict);
			break;
		case 6011:
			tranMessageBody = getTranMessageBody_6011(parmaKeyDict);
			break;
		case 6014:
			tranMessageBody = getTranMessageBody_6014(parmaKeyDict);
			break;
		case 6027:
			tranMessageBody = getTranMessageBody_6027(parmaKeyDict);
			break;
		case 6031:
			tranMessageBody = getTranMessageBody_6031(parmaKeyDict);
			break;
		case 6033:
			tranMessageBody = getTranMessageBody_6033(parmaKeyDict);
			break;
		case 6034:
			tranMessageBody = getTranMessageBody_6034(parmaKeyDict);
			break;
		case 6040:
			tranMessageBody = getTranMessageBody_6048(parmaKeyDict);
			break;
		case 6041:
			tranMessageBody = getTranMessageBody_6048(parmaKeyDict);
			break;
		case 6042:
			tranMessageBody = getTranMessageBody_6048(parmaKeyDict);
			break;
		case 6043:
			tranMessageBody = getTranMessageBody_6048(parmaKeyDict);
			break;
		case 6044:
			tranMessageBody = getTranMessageBody_6048(parmaKeyDict);
			break;
		case 6048:
			tranMessageBody = getTranMessageBody_6048(parmaKeyDict);
			break;
		case 6050:
			tranMessageBody = getTranMessageBody_6050(parmaKeyDict);
			break;
		case 6055:
			tranMessageBody = getTranMessageBody_6055(parmaKeyDict);
			break;
		case 6056:
			tranMessageBody = getTranMessageBody_6056(parmaKeyDict);
			break;
		case 6063:
			tranMessageBody = getTranMessageBody_6063(parmaKeyDict);
			break;
		case 6064:
			tranMessageBody = getTranMessageBody_6064(parmaKeyDict);
			break;
		case 6065:
			tranMessageBody = getTranMessageBody_6065(parmaKeyDict);
			break;
		case 6066:
			tranMessageBody = getTranMessageBody_6066(parmaKeyDict);
			break;
		case 6067:
			tranMessageBody = getTranMessageBody_6067(parmaKeyDict);
			break;
		case 6068:
			tranMessageBody = getTranMessageBody_6068(parmaKeyDict);
			break;
		case 6069:
			tranMessageBody = getTranMessageBody_6069(parmaKeyDict);
			break;
		case 6071:
			tranMessageBody = getTranMessageBody_6071(parmaKeyDict);
			break;
		case 6072:
			tranMessageBody = getTranMessageBody_6072(parmaKeyDict);
			break;
		case 6073:
			tranMessageBody = getTranMessageBody_6073(parmaKeyDict);
			break;
		case 6037:
			tranMessageBody = getTranMessageBody_6037(parmaKeyDict);
			break;
		case 6077:
			tranMessageBody = getTranMessageBody_6077(parmaKeyDict);
			break;
		case 6082:
			tranMessageBody = getTranMessageBody_6082(parmaKeyDict);
			break;
		case 6083:
			tranMessageBody = getTranMessageBody_6083(parmaKeyDict);
			break;
		case 6084:
			tranMessageBody = getTranMessageBody_6084(parmaKeyDict);
			break;
		case 6085:
			tranMessageBody = getTranMessageBody_6085(parmaKeyDict);
			break;
		case 6089:
			tranMessageBody = getTranMessageBody_6089(parmaKeyDict);
			break;
		// case
		// 6070:tranMessageBody=getTranMessageBody_6070(parmaKeyDict);break;
		case 6079:
			tranMessageBody = getTranMessageBody_6079(parmaKeyDict);
			break;
		case 6080:
			tranMessageBody = getTranMessageBody_6079(parmaKeyDict);
			break;
		case 6093:
			tranMessageBody = getTranMessageBody_6093(parmaKeyDict);
			break;
		case 6098:
			tranMessageBody = getTranMessageBody_6098(parmaKeyDict);
			break;
		case 6099:
			tranMessageBody = getTranMessageBody_6099(parmaKeyDict);
			break;

		default:
			tranMessageBody = "交易码未配置，请联系银行技术人员更新接口API";
		}

		return tranMessageBody;
	}

	/* 根据交易码调用不同的交易返回报文生成方法 */
	/*
	 * private String getRetMessageBody(HashMap parmaKeyDict) { String
	 * tranMessageBody=""; String
	 * hTranFunc=(String)parmaKeyDict.get("TranFunc");
	 * 
	 * switch(hTranFunc) { case
	 * "6000":tranMessageBody=getRetMessageBody_6000(parmaKeyDict);break; }
	 * 
	 * return tranMessageBody; }
	 */
	/* 生成6000交易的报文体:会员注册 */
	private String getTranMessageBody_6000(HashMap parmaKeyDict) {

		String bFuncFlag = ""; // 功能标志
		String bSupAcctId = ""; // 资金汇总账号
		String bThirdCustId = ""; // 交易网会员代码
		String bCustProperty = ""; // 会员属性
		String bNickName = ""; // 用户昵称
		String bMobilePhone = ""; // 手机号码
		String bEmail = ""; // Email
		String bReserve = ""; // 保留域

		if (parmaKeyDict.containsKey("FuncFlag")) {
			bFuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("SupAcctId")) {
			bSupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			bThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustProperty")) {
			bCustProperty = (String) parmaKeyDict.get("CustProperty");
		}

		if (parmaKeyDict.containsKey("NickName")) {
			bNickName = (String) parmaKeyDict.get("NickName");
		}

		if (parmaKeyDict.containsKey("MobilePhone")) {
			bMobilePhone = (String) parmaKeyDict.get("MobilePhone");
		}

		if (parmaKeyDict.containsKey("Email")) {
			bEmail = (String) parmaKeyDict.get("Email");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			bReserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = bFuncFlag + "&" + bSupAcctId + "&"
				+ bThirdCustId + "&" + bCustProperty + "&" + bNickName + "&"
				+ bMobilePhone + "&" + bEmail + "&" + bReserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6055(HashMap parmaKeyDict) {

		String SupAcctId = "";
		String CustAcctId = "";
		String ThirdCustId = "";
		String CustName = "";
		String IdType = "";
		String IdCode = "";
		String AcctId = "";
		String BankType = "";
		String BankName = "";
		String BankCode = "";
		String SBankCode = "";
		String MobilePhone = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}

		if (parmaKeyDict.containsKey("IdType")) {
			IdType = (String) parmaKeyDict.get("IdType");
		}

		if (parmaKeyDict.containsKey("IdCode")) {
			IdCode = (String) parmaKeyDict.get("IdCode");
		}

		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("BankType")) {
			BankType = (String) parmaKeyDict.get("BankType");
		}

		if (parmaKeyDict.containsKey("BankName")) {
			BankName = (String) parmaKeyDict.get("BankName");
		}

		if (parmaKeyDict.containsKey("BankCode")) {
			BankCode = (String) parmaKeyDict.get("BankCode");
		}

		if (parmaKeyDict.containsKey("SBankCode")) {
			SBankCode = (String) parmaKeyDict.get("SBankCode");
		}

		if (parmaKeyDict.containsKey("MobilePhone")) {
			MobilePhone = (String) parmaKeyDict.get("MobilePhone");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
				+ ThirdCustId + "&" + CustName + "&" + IdType + "&" + IdCode
				+ "&" + AcctId + "&" + BankType + "&" + BankName + "&"
				+ BankCode + "&" + SBankCode + "&" + MobilePhone + "&"
				+ Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6063(HashMap parmaKeyDict) {

		String FuncFlag = "";
		String SupAcctId = "";
		String CustAcctId = "";
		String ThirdCustId = "";
		String CustName = "";
		String IdType = "";
		String IdCode = "";
		String AcctId = "";
		String BankName = "";
		String BankCode = "";
		String SBankCode = "";
		String MobilePhone = "";
		String Reserve = "";
		String TelePhone = "";
		String Email = "";
		String Address = "";
		String CPFlag = "";
		String BankFlag = "";
		String NickName = "";
		String AcctName = "";

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}

		if (parmaKeyDict.containsKey("IdType")) {
			IdType = (String) parmaKeyDict.get("IdType");
		}

		if (parmaKeyDict.containsKey("IdCode")) {
			IdCode = (String) parmaKeyDict.get("IdCode");
		}

		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("BankFlag")) {
			BankFlag = (String) parmaKeyDict.get("BankFlag");
		}

		if (parmaKeyDict.containsKey("BankName")) {
			BankName = (String) parmaKeyDict.get("BankName");
		}

		if (parmaKeyDict.containsKey("BankCode")) {
			BankCode = (String) parmaKeyDict.get("BankCode");
		}

		if (parmaKeyDict.containsKey("SBankCode")) {
			SBankCode = (String) parmaKeyDict.get("SBankCode");
		}

		if (parmaKeyDict.containsKey("MobilePhone")) {
			MobilePhone = (String) parmaKeyDict.get("MobilePhone");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		if (parmaKeyDict.containsKey("TelePhone")) {
			TelePhone = (String) parmaKeyDict.get("TelePhone");
		}

		if (parmaKeyDict.containsKey("Email")) {
			Email = (String) parmaKeyDict.get("Email");
		}

		if (parmaKeyDict.containsKey("CPFlag")) {
			CPFlag = (String) parmaKeyDict.get("CPFlag");
		}

		if (parmaKeyDict.containsKey("NickName")) {
			NickName = (String) parmaKeyDict.get("NickName");
		}

		if (parmaKeyDict.containsKey("AcctName")) {
			AcctName = (String) parmaKeyDict.get("AcctName");
		}

		if (parmaKeyDict.containsKey("Address")) {
			Address = (String) parmaKeyDict.get("Address");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + CustAcctId
				+ "&" + CustName + "&" + NickName + "&" + IdType + "&" + IdCode
				+ "&" + ThirdCustId + "&" + MobilePhone + "&" + TelePhone + "&"
				+ Email + "&" + Address + "&" + CPFlag + "&" + BankFlag + "&"
				+ AcctName + "&" + AcctId + "&" + BankName + "&" + BankCode
				+ "&" + SBankCode + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6064(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String AcctId = "";
		String TranAmount = "";
		String CcyCode = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&"
				+ CustAcctId + "&" + AcctId + "&" + TranAmount + "&" + CcyCode
				+ "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6066(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String ThirdCustId = "";
		String CustName = "";
		String IdType = "";
		String IdCode = "";
		String AcctId = "";
		String BankType = "";
		String BankName = "";
		String BankCode = "";
		String SBankCode = "";
		String MobilePhone = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}

		if (parmaKeyDict.containsKey("IdType")) {
			IdType = (String) parmaKeyDict.get("IdType");
		}

		if (parmaKeyDict.containsKey("IdCode")) {
			IdCode = (String) parmaKeyDict.get("IdCode");
		}

		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("BankType")) {
			BankType = (String) parmaKeyDict.get("BankType");
		}

		if (parmaKeyDict.containsKey("BankName")) {
			BankName = (String) parmaKeyDict.get("BankName");
		}

		if (parmaKeyDict.containsKey("BankCode")) {
			BankCode = (String) parmaKeyDict.get("BankCode");
		}

		if (parmaKeyDict.containsKey("SBankCode")) {
			SBankCode = (String) parmaKeyDict.get("SBankCode");
		}

		if (parmaKeyDict.containsKey("MobilePhone")) {
			MobilePhone = (String) parmaKeyDict.get("MobilePhone");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
				+ ThirdCustId + "&" + CustName + "&" + IdType + "&" + IdCode
				+ "&" + AcctId + "&" + BankType + "&" + BankName + "&"
				+ BankCode + "&" + SBankCode + "&" + MobilePhone + "&"
				+ Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6067(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String TranAmount = "";
		String AcctId = "";
		String MessageCode = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("MessageCode")) {
			MessageCode = (String) parmaKeyDict.get("MessageCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&"
				+ CustAcctId + "&" + AcctId + "&" + MessageCode + "&" + Reserve
				+ "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6056(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String TranAmount = "";
		String CcyCode = "";
		String Note = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}

		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
				+ ThirdCustId + "&" + TranAmount + "&" + CcyCode + "&" + Note
				+ "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6033(HashMap parmaKeyDict) {
		String TranWebName = "";
		String CustAcctId = "";
		String IdType = "";
		String IdCode = "";
		String ThirdCustId = "";
		String CustName = "";
		String SupAcctId = "";
		String OutAcctId = "";
		String OutAcctIdName = "";
		String CcyCode = "";
		String TranAmount = "";
		String Note = "";
		String Reserve = "";
		String WebSign = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}

		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		if (parmaKeyDict.containsKey("TranWebName")) {
			TranWebName = (String) parmaKeyDict.get("TranWebName");
		}

		if (parmaKeyDict.containsKey("IdType")) {
			IdType = (String) parmaKeyDict.get("IdType");
		}

		if (parmaKeyDict.containsKey("IdCode")) {
			IdCode = (String) parmaKeyDict.get("IdCode");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}
		if (parmaKeyDict.containsKey("OutAcctIdName")) {
			OutAcctIdName = (String) parmaKeyDict.get("OutAcctIdName");
		}
		if (parmaKeyDict.containsKey("OutAcctId")) {
			OutAcctId = (String) parmaKeyDict.get("OutAcctId");
		}
		if (parmaKeyDict.containsKey("WebSign")) {
			WebSign = (String) parmaKeyDict.get("WebSign");
		}

		String tranMessageBody = TranWebName + "&" + CustAcctId + "&" + IdType
				+ "&" + IdCode + "&" + ThirdCustId + "&" + CustName + "&"
				+ SupAcctId + "&" + OutAcctId + "&" + OutAcctIdName + "&"
				+ CcyCode + "&" + TranAmount + "&" + Note + "&" + Reserve + "&"
				+ WebSign + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6007(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String FuncFlag = "";
		String HandFee = "";
		String TranAmount = "";
		String CcyCode = "";
		String ThirdHtId = "";
		String ThirdHtMsg = "";
		String Note = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("HandFee")) {
			HandFee = (String) parmaKeyDict.get("HandFee");
		}

		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}

		if (parmaKeyDict.containsKey("ThirdHtId")) {
			ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
		}

		if (parmaKeyDict.containsKey("ThirdHtMsg")) {
			ThirdHtMsg = (String) parmaKeyDict.get("ThirdHtMsg");
		}

		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + CustAcctId
				+ "&" + ThirdCustId + "&" + TranAmount + "&" + HandFee + "&"
				+ CcyCode + "&" + ThirdHtId + "&" + ThirdHtMsg + "&" + Note
				+ "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6008(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String CustName = "";
		String TranAmount = "";
		String CcyCode = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
				+ ThirdCustId + "&" + CustName + "&" + TranAmount + "&"
				+ CcyCode + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6031(HashMap parmaKeyDict) {
		String FuncFlag = "";
		String OutCustAcctId = "";
		String SupAcctId = "";
		String OutThirdCustId = "";
		String OutCustName = "";
		String InCustAcctId = "";
		String InThirdCustId = "";
		String InCustName = "";
		String TranAmount = "";
		String TranFee = "";
		String TranType = "";
		String CcyCode = "";
		String ThirdHtId = "";
		String ThirdHtMsg = "";
		String Note = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("OutCustAcctId")) {
			OutCustAcctId = (String) parmaKeyDict.get("OutCustAcctId");
		}

		if (parmaKeyDict.containsKey("OutCustName")) {
			OutCustName = (String) parmaKeyDict.get("OutCustName");
		}

		if (parmaKeyDict.containsKey("OutThirdCustId")) {
			OutThirdCustId = (String) parmaKeyDict.get("OutThirdCustId");
		}

		if (parmaKeyDict.containsKey("InCustAcctId")) {
			InCustAcctId = (String) parmaKeyDict.get("InCustAcctId");
		}

		if (parmaKeyDict.containsKey("InThirdCustId")) {
			InThirdCustId = (String) parmaKeyDict.get("InThirdCustId");
		}

		if (parmaKeyDict.containsKey("InCustName")) {
			InCustName = (String) parmaKeyDict.get("InCustName");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("TranFee")) {
			TranFee = (String) parmaKeyDict.get("TranFee");
		}
		if (parmaKeyDict.containsKey("TranType")) {
			TranType = (String) parmaKeyDict.get("TranType");
		}
		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}
		if (parmaKeyDict.containsKey("ThirdHtId")) {
			ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
		}
		if (parmaKeyDict.containsKey("ThirdHtMsg")) {
			ThirdHtMsg = (String) parmaKeyDict.get("ThirdHtMsg");
		}
		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&"
				+ OutCustAcctId + "&" + OutThirdCustId + "&" + OutCustName
				+ "&" + InCustAcctId + "&" + InThirdCustId + "&" + InCustName
				+ "&" + TranAmount + "&" + TranFee + "&" + TranType + "&"
				+ CcyCode + "&" + ThirdHtId + "&" + ThirdHtMsg + "&" + Note
				+ "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6034(HashMap parmaKeyDict) {
		String FuncFlag = "";
		String OutCustAcctId = "";
		String SupAcctId = "";
		String OutThirdCustId = "";
		String OutCustName = "";
		String InCustAcctId = "";
		String InThirdCustId = "";
		String InCustName = "";
		String TranAmount = "";
		String TranFee = "";
		String TranType = "";
		String CcyCode = "";
		String ThirdHtId = "";
		String ThirdHtMsg = "";
		String Note = "";
		String Reserve = "";
		String WebSign = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("OutCustAcctId")) {
			OutCustAcctId = (String) parmaKeyDict.get("OutCustAcctId");
		}

		if (parmaKeyDict.containsKey("OutCustName")) {
			OutCustName = (String) parmaKeyDict.get("OutCustName");
		}

		if (parmaKeyDict.containsKey("OutThirdCustId")) {
			OutThirdCustId = (String) parmaKeyDict.get("OutThirdCustId");
		}

		if (parmaKeyDict.containsKey("InCustAcctId")) {
			InCustAcctId = (String) parmaKeyDict.get("InCustAcctId");
		}

		if (parmaKeyDict.containsKey("InThirdCustId")) {
			InThirdCustId = (String) parmaKeyDict.get("InThirdCustId");
		}

		if (parmaKeyDict.containsKey("InCustName")) {
			InCustName = (String) parmaKeyDict.get("InCustName");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("TranFee")) {
			TranFee = (String) parmaKeyDict.get("TranFee");
		}
		if (parmaKeyDict.containsKey("TranType")) {
			TranType = (String) parmaKeyDict.get("TranType");
		}
		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}
		if (parmaKeyDict.containsKey("ThirdHtId")) {
			ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
		}
		if (parmaKeyDict.containsKey("ThirdHtMsg")) {
			ThirdHtMsg = (String) parmaKeyDict.get("ThirdHtMsg");
		}
		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		if (parmaKeyDict.containsKey("WebSign")) {
			WebSign = (String) parmaKeyDict.get("WebSign");
		}

		String tranMessageBody = FuncFlag + "&" + OutCustAcctId + "&"
				+ SupAcctId + "&" + OutThirdCustId + "&" + OutCustName + "&"
				+ InCustAcctId + "&" + InThirdCustId + "&" + InCustName + "&"
				+ TranAmount + "&" + TranFee + "&" + TranType + "&" + CcyCode
				+ "&" + ThirdHtId + "&" + ThirdHtMsg + "&" + Note + "&"
				+ Reserve + "&" + WebSign + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6010(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String SelectFlag = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("SelectFlag")) {
			SelectFlag = (String) parmaKeyDict.get("SelectFlag");
		}

		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
				+ SelectFlag + "&" + PageNum + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6014(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String FuncFlag = "";
		String OrigThirdLogNo = "";
		String TranDate = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("OrigThirdLogNo")) {
			OrigThirdLogNo = (String) parmaKeyDict.get("OrigThirdLogNo");
		}

		if (parmaKeyDict.containsKey("TranDate")) {
			TranDate = (String) parmaKeyDict.get("TranDate");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + FuncFlag + "&"
				+ OrigThirdLogNo + "&" + TranDate + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6048(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String FuncFlag = "";
		String BeginDate = "";
		String EndDate = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}

		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + BeginDate
				+ "&" + EndDate + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6050(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String FuncFlag = "";
		String BeginDate = "";
		String EndDate = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}

		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}
		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + BeginDate
				+ "&" + EndDate + "&" + PageNum + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6027(HashMap parmaKeyDict) {
		String BankNo = "";
		String KeyWord = "";
		String BankName = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("BankNo")) {
			BankNo = (String) parmaKeyDict.get("BankNo");
		}

		if (parmaKeyDict.containsKey("KeyWord")) {
			KeyWord = (String) parmaKeyDict.get("KeyWord");
		}

		if (parmaKeyDict.containsKey("BankName")) {
			BankName = (String) parmaKeyDict.get("BankName");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = BankNo + "&" + KeyWord + "&" + BankName + "&"
				+ Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6072(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String SelectFlag = "";
		String FuncFlag = "";
		String BeginDate = "";
		String EndDate = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("SelectFlag")) {
			SelectFlag = (String) parmaKeyDict.get("SelectFlag");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}

		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}
		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + CustAcctId
				+ "&" + SelectFlag + "&" + BeginDate + "&" + EndDate + "&"
				+ PageNum + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6037(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&" + Reserve
				+ "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6073(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String SelectFlag = "";
		String FuncFlag = "";
		String BeginDate = "";
		String EndDate = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("SelectFlag")) {
			SelectFlag = (String) parmaKeyDict.get("SelectFlag");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}

		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}
		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + CustAcctId
				+ "&" + SelectFlag + "&" + BeginDate + "&" + EndDate + "&"
				+ PageNum + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6077(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String FuncFlag = "";
		String OrigThirdLogNo = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("OrigThirdLogNo")) {
			OrigThirdLogNo = (String) parmaKeyDict.get("OrigThirdLogNo");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + FuncFlag + "&"
				+ OrigThirdLogNo + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6071(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String ThirdCustId = "";
		String AcctId = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("SelectFlag")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&"
				+ CustAcctId + "&" + AcctId + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6011(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6065(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String FuncFlag = "";
		String AcctId = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + ThirdCustId
				+ "&" + CustAcctId + "&" + AcctId + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6068(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String CustName = "";
		String IdType = "";
		String IdCode = "";
		String TranAmount = "";
		String ProductCode = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}

		if (parmaKeyDict.containsKey("IdType")) {
			IdType = (String) parmaKeyDict.get("IdType");
		}

		if (parmaKeyDict.containsKey("IdCode")) {
			IdCode = (String) parmaKeyDict.get("IdCode");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("ProductCode")) {
			ProductCode = (String) parmaKeyDict.get("ProductCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&" + CustName
				+ "&" + IdType + "&" + IdCode + "&" + TranAmount + "&"
				+ ProductCode + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6069(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String ClientNo = "";
		String TranAmount = "";
		String ProductCode = "";
		String TradeType = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ClientNo")) {
			ClientNo = (String) parmaKeyDict.get("ClientNo");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("TradeType")) {
			TradeType = (String) parmaKeyDict.get("TradeType");
		}

		if (parmaKeyDict.containsKey("ProductCode")) {
			ProductCode = (String) parmaKeyDict.get("ProductCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&" + ClientNo
				+ "&" + TranAmount + "&" + ProductCode + "&" + TradeType + "&"
				+ Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6040(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ProductCode = "";
		String BeginDate = "";
		String EndDate = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}

		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}

		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}

		if (parmaKeyDict.containsKey("ProductCode")) {
			ProductCode = (String) parmaKeyDict.get("ProductCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		String tranMessageBody = SupAcctId + "&" + ProductCode + "&"
				+ BeginDate + "&" + EndDate + "&" + PageNum + "&" + Reserve
				+ "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6043(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String ClientNo = "";
		String ProductCode = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ClientNo")) {
			ClientNo = (String) parmaKeyDict.get("ClientNo");
		}

		if (parmaKeyDict.containsKey("ProductCode")) {
			ProductCode = (String) parmaKeyDict.get("ProductCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&" + ClientNo
				+ "&" + ProductCode + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6044(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String ClientNo = "";
		String ProductCode = "";
		String BeginDate = "";
		String EndDate = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}

		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}

		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}

		if (parmaKeyDict.containsKey("ProductCode")) {
			ProductCode = (String) parmaKeyDict.get("ProductCode");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}
		if (parmaKeyDict.containsKey("ClientNo")) {
			ClientNo = (String) parmaKeyDict.get("ClientNo");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&" + ClientNo
				+ "&" + ProductCode + "&" + BeginDate + "&" + EndDate + "&"
				+ PageNum + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6041(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String FuncFlag = "";
		String SelectFlag = "";
		String BeginDate = "";
		String EndDate = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}

		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}

		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}
		if (parmaKeyDict.containsKey("SelectFlag")) {
			SelectFlag = (String) parmaKeyDict.get("SelectFlag");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		String tranMessageBody = FuncFlag + "&" + SupAcctId + "&" + CustAcctId
				+ "&" + SelectFlag + "&" + BeginDate + "&" + EndDate + "&"
				+ PageNum + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6042(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String OrigThirdLogNo = "";
		String TranDate = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("OrigThirdLogNo")) {
			OrigThirdLogNo = (String) parmaKeyDict.get("OrigThirdLogNo");
		}

		if (parmaKeyDict.containsKey("TranDate")) {
			TranDate = (String) parmaKeyDict.get("TranDate");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}
		String tranMessageBody = SupAcctId + "&" + OrigThirdLogNo + "&"
				+ TranDate + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getSignMessageBody_6005(HashMap parmaKeyDict) {
		String TranWebName = "";
		String CustAcctId = "";
		String IdType = "";
		String IdCode = "";
		String ThirdCustId = "";
		String CustName = "";
		String SupAcctId = "";
		String OutAcctId = "";
		String OutAcctIdName = "";
		String CcyCode = "";
		String TranAmount = "";
		String Note = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}

		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		if (parmaKeyDict.containsKey("TranWebName")) {
			TranWebName = (String) parmaKeyDict.get("TranWebName");
		}

		if (parmaKeyDict.containsKey("IdType")) {
			IdType = (String) parmaKeyDict.get("IdType");
		}

		if (parmaKeyDict.containsKey("IdCode")) {
			IdCode = (String) parmaKeyDict.get("IdCode");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}
		if (parmaKeyDict.containsKey("OutAcctIdName")) {
			OutAcctIdName = (String) parmaKeyDict.get("OutAcctIdName");
		}
		if (parmaKeyDict.containsKey("OutAcctId")) {
			OutAcctId = (String) parmaKeyDict.get("OutAcctId");
		}

		String tranMessageBody = TranWebName + "&" + CustAcctId + "&" + IdType
				+ "&" + IdCode + "&" + ThirdCustId + "&" + CustName + "&"
				+ SupAcctId + "&" + OutAcctId + "&" + OutAcctIdName + "&"
				+ CcyCode + "&" + TranAmount + "&" + Note + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getSignMessageBody_6006(HashMap parmaKeyDict) {
		String FuncFlag = "";
		String OutCustAcctId = "";
		String SupAcctId = "";
		String OutThirdCustId = "";
		String OutCustName = "";
		String InCustAcctId = "";
		String InThirdCustId = "";
		String InCustName = "";
		String TranAmount = "";
		String TranFee = "";
		String TranType = "";
		String CcyCode = "";
		String ThirdHtId = "";
		String ThirdHtMsg = "";
		String Note = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("FuncFlag")) {
			FuncFlag = (String) parmaKeyDict.get("FuncFlag");
		}

		if (parmaKeyDict.containsKey("OutCustAcctId")) {
			OutCustAcctId = (String) parmaKeyDict.get("OutCustAcctId");
		}

		if (parmaKeyDict.containsKey("OutCustName")) {
			OutCustName = (String) parmaKeyDict.get("OutCustName");
		}

		if (parmaKeyDict.containsKey("OutThirdCustId")) {
			OutThirdCustId = (String) parmaKeyDict.get("OutThirdCustId");
		}

		if (parmaKeyDict.containsKey("InCustAcctId")) {
			InCustAcctId = (String) parmaKeyDict.get("InCustAcctId");
		}

		if (parmaKeyDict.containsKey("InThirdCustId")) {
			InThirdCustId = (String) parmaKeyDict.get("InThirdCustId");
		}

		if (parmaKeyDict.containsKey("InCustName")) {
			InCustName = (String) parmaKeyDict.get("InCustName");
		}

		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("TranFee")) {
			TranFee = (String) parmaKeyDict.get("TranFee");
		}
		if (parmaKeyDict.containsKey("TranType")) {
			TranType = (String) parmaKeyDict.get("TranType");
		}
		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}
		if (parmaKeyDict.containsKey("ThirdHtId")) {
			ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
		}
		if (parmaKeyDict.containsKey("ThirdHtMsg")) {
			ThirdHtMsg = (String) parmaKeyDict.get("ThirdHtMsg");
		}
		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = FuncFlag + "&" + OutCustAcctId + "&"
				+ SupAcctId + "&" + OutThirdCustId + "&" + OutCustName + "&"
				+ InCustAcctId + "&" + InThirdCustId + "&" + InCustName + "&"
				+ TranAmount + "&" + TranFee + "&" + TranType + "&" + CcyCode
				+ "&" + ThirdHtId + "&" + ThirdHtMsg + "&" + Note + "&"
				+ Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6089(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String CustName = "";
		String IdType = "";
		String IdCode = "";
		String AcctId = "";
		String BankType = "";
		String BankName = "";
		String BankCode = "";
		String SBankCode = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}
		if (parmaKeyDict.containsKey("IdType")) {
			IdType = (String) parmaKeyDict.get("IdType");
		}
		if (parmaKeyDict.containsKey("IdCode")) {
			IdCode = (String) parmaKeyDict.get("IdCode");
		}
		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("BankType")) {
			BankType = (String) parmaKeyDict.get("BankType");
		}

		if (parmaKeyDict.containsKey("BankName")) {
			BankName = (String) parmaKeyDict.get("BankName");
		}

		if (parmaKeyDict.containsKey("BankCode")) {
			BankCode = (String) parmaKeyDict.get("BankCode");
		}

		if (parmaKeyDict.containsKey("SBankCode")) {
			SBankCode = (String) parmaKeyDict.get("SBankCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&"
				+ CustAcctId + "&" + CustName + "&" + IdType + "&" + IdCode
				+ "&" + AcctId + "&" + BankType + "&" + BankName + "&"
				+ BankCode + "&" + SBankCode + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6082(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String TranType = "";
		String TranAmount = "";
		String AcctId = "";
		String ThirdHtId = "";
		String TranNote = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("TranType")) {
			TranType = (String) parmaKeyDict.get("TranType");
		}
		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}
		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("ThirdHtId")) {
			ThirdHtId = (String) parmaKeyDict.get("ThirdHtId");
		}

		if (parmaKeyDict.containsKey("TranNote")) {
			TranNote = (String) parmaKeyDict.get("TranNote");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&"
				+ CustAcctId + "&" + TranType + "&" + TranAmount + "&" + AcctId
				+ "&" + ThirdHtId + "&" + TranNote + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6083(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String ModifiedType = "";
		String NewMobilePhone = "";
		String AcctId = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ModifiedType")) {
			ModifiedType = (String) parmaKeyDict.get("ModifiedType");
		}
		if (parmaKeyDict.containsKey("NewMobilePhone")) {
			NewMobilePhone = (String) parmaKeyDict.get("NewMobilePhone");
		}
		if (parmaKeyDict.containsKey("AcctId")) {
			AcctId = (String) parmaKeyDict.get("AcctId");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&"
				+ CustAcctId + "&" + ModifiedType + "&" + NewMobilePhone + "&"
				+ AcctId + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6084(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String ThirdCustId = "";
		String CustAcctId = "";
		String ModifiedType = "";
		String SerialNo = "";
		String MessageCode = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ModifiedType")) {
			ModifiedType = (String) parmaKeyDict.get("ModifiedType");
		}
		if (parmaKeyDict.containsKey("SerialNo")) {
			SerialNo = (String) parmaKeyDict.get("SerialNo");
		}
		if (parmaKeyDict.containsKey("MessageCode")) {
			MessageCode = (String) parmaKeyDict.get("MessageCode");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + ThirdCustId + "&"
				+ CustAcctId + "&" + ModifiedType + "&" + SerialNo + "&"
				+ MessageCode + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6085(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String ThirdCustId = "";
		String CustName = "";
		String OutAcctId = "";
		String OutAcctIdName = "";
		String CcyCode = "";
		String TranAmount = "";
		String HandFee = "";
		String SerialNo = "";
		String MessageCode = "";
		String Note = "";
		String Reserve = "";
		String WebSign = "";
		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("ThirdCustId")) {
			ThirdCustId = (String) parmaKeyDict.get("ThirdCustId");
		}

		if (parmaKeyDict.containsKey("CustName")) {
			CustName = (String) parmaKeyDict.get("CustName");
		}
		if (parmaKeyDict.containsKey("OutAcctIdName")) {
			OutAcctIdName = (String) parmaKeyDict.get("OutAcctIdName");
		}
		if (parmaKeyDict.containsKey("OutAcctId")) {
			OutAcctId = (String) parmaKeyDict.get("OutAcctId");
		}
		if (parmaKeyDict.containsKey("TranAmount")) {
			TranAmount = (String) parmaKeyDict.get("TranAmount");
		}

		if (parmaKeyDict.containsKey("CcyCode")) {
			CcyCode = (String) parmaKeyDict.get("CcyCode");
		}

		if (parmaKeyDict.containsKey("Note")) {
			Note = (String) parmaKeyDict.get("Note");
		}

		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		if (parmaKeyDict.containsKey("HandFee")) {
			HandFee = (String) parmaKeyDict.get("HandFee");
		}

		if (parmaKeyDict.containsKey("SerialNo")) {
			SerialNo = (String) parmaKeyDict.get("SerialNo");
		}

		if (parmaKeyDict.containsKey("MessageCode")) {
			MessageCode = (String) parmaKeyDict.get("MessageCode");
		}
		if (parmaKeyDict.containsKey("WebSign")) {
			WebSign = (String) parmaKeyDict.get("WebSign");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
				+ ThirdCustId + "&" + CustName + "&" + OutAcctId + "&"
				+ OutAcctIdName + "&" + CcyCode + "&" + TranAmount + "&"
				+ HandFee + "&" + SerialNo + "&" + MessageCode + "&" + Note
				+ "&" + Reserve + "&" + WebSign + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6079(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String SelectFlag = "";
		String BeginDate = "";
		String EndDate = "";
		String PageNum = "";
		String RecordMax = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("SelectFlag")) {
			SelectFlag = (String) parmaKeyDict.get("SelectFlag");
		}

		if (parmaKeyDict.containsKey("BeginDate")) {
			BeginDate = (String) parmaKeyDict.get("BeginDate");
		}
		if (parmaKeyDict.containsKey("EndDate")) {
			EndDate = (String) parmaKeyDict.get("EndDate");
		}
		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}
		if (parmaKeyDict.containsKey("RecordMax")) {
			RecordMax = (String) parmaKeyDict.get("RecordMax");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
				+ SelectFlag + "&" + BeginDate + "&" + EndDate + "&" + PageNum
				+ "&" + RecordMax + "&" + Reserve + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6093(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&" + Reserve
				+ "&";

		// String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
		// + SelectFlag + "&" + Reserve + "&" + PageNum + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6098(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String SelectFlag = "";
		String PageNum = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}

		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}

		if (parmaKeyDict.containsKey("SelectFlag")) {
			SelectFlag = (String) parmaKeyDict.get("SelectFlag");
		}

		if (parmaKeyDict.containsKey("PageNum")) {
			PageNum = (String) parmaKeyDict.get("PageNum");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SelectFlag + "&" + SupAcctId + "&"
				+ CustAcctId + "&" + PageNum + "&" + Reserve + "&";

		// String tranMessageBody = SupAcctId + "&" + CustAcctId + "&"
		// + SelectFlag + "&" + Reserve + "&" + PageNum + "&";

		return tranMessageBody;
	}

	private String getTranMessageBody_6099(HashMap parmaKeyDict) {
		String SupAcctId = "";
		String CustAcctId = "";
		String Reserve = "";

		if (parmaKeyDict.containsKey("SupAcctId")) {
			SupAcctId = (String) parmaKeyDict.get("SupAcctId");
		}
		if (parmaKeyDict.containsKey("CustAcctId")) {
			CustAcctId = (String) parmaKeyDict.get("CustAcctId");
		}
		if (parmaKeyDict.containsKey("Reserve")) {
			Reserve = (String) parmaKeyDict.get("Reserve");
		}

		String tranMessageBody = SupAcctId + "&" + CustAcctId + "&" + Reserve
				+ "&";

		return tranMessageBody;
	}

	private void spiltMessage(HashMap retKeyDict) {
		int tranFunc = Integer.parseInt((String) retKeyDict.get("TranFunc"));
		switch (tranFunc) {
		case 6000:
			spiltMessage_6000(retKeyDict);
			break;
		case 6005:
			spiltMessage_HandFee(retKeyDict);
			break;
		case 6006:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6007:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6008:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6010:
			spiltMessage_6010(retKeyDict);
			break;
		case 6011:
			spiltMessage_6011(retKeyDict);
			break;
		case 6014:
			spiltMessage_6014(retKeyDict);
			break;
		case 6027:
			spiltMessage_6027(retKeyDict);
			break;
		case 6031:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6033:
			spiltMessage_HandFee(retKeyDict);
			break;
		case 6034:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6040:
			spiltMessage_6040(retKeyDict);
			break;
		case 6041:
			spiltMessage_6041(retKeyDict);
			break;
		case 6042:
			spiltMessage_6042(retKeyDict);
			break;
		case 6043:
			spiltMessage_6043(retKeyDict);
			break;
		case 6044:
			spiltMessage_6044(retKeyDict);
			break;
		case 6048:
			spiltMessage_6048(retKeyDict);
			break;
		case 6050:
			spiltMessage_6050(retKeyDict);
			break;
		case 6055:
			spiltMessage_Reserve(retKeyDict);
			break;
		case 6056:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6064:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6065:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6066:
			spiltMessage_Reserve(retKeyDict);
			break;
		case 6067:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6072:
			spiltMessage_6072(retKeyDict);
			break;
		case 6073:
			spiltMessage_6073(retKeyDict);
			break;
		case 6037:
			spiltMessage_6037(retKeyDict);
			break;
		case 6077:
			spiltMessage_FrontLogNo(retKeyDict);
			break;
		case 6082:
			spiltMessage_6082(retKeyDict);
			break;
		case 6083:
			spiltMessage_6083(retKeyDict);
			break;
		case 6084:
			spiltMessage_6084(retKeyDict);
			break;
		case 6085:
			spiltMessage_6085(retKeyDict);
			break;
		case 6089:
			spiltMessage_6089(retKeyDict);
			break;
		// case 6070:spiltMessage_6070(retKeyDict);break;
		case 6079:
			spiltMessage_6079(retKeyDict);
			break;
		case 6080:
			spiltMessage_6080(retKeyDict);
			break;
		case 6093:
			spiltMessage_6093(retKeyDict);
			break;
		case 6098:
			spiltMessage_6098(retKeyDict);
			break;
		}
	}

	private void spiltMessage_6098(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {

			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];

			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 2 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);

			String[] arr = StringUtils.split(ArrayContent, "&");
			List<List<String>> list = new ArrayList<List<String>>();
			List<String> l = Arrays.asList(arr);
			List<Integer> indexArray = new ArrayList<Integer>();
			for (int i = 0; i < l.size(); i++) {
				if (StringUtils.equals(l.get(i), WalletConstant.SUP_ACCT_ID)) {
					indexArray.add(i);
				}
			}
			int size = indexArray.size();
			for (int j = 0; j < size; j++) {
				List<String> m = null;
				if (j < size - 1) {
					m = l.subList(indexArray.get(j), indexArray.get(j + 1) - 1);
				} else {
					m = l.subList(indexArray.get(j), arr.length - 1);
				}
				list.add(m);
			}
			retKeyDict.put("ArrayContent", list);

			StringUtils.lastIndexOf(ArrayContent, WalletConstant.SUP_ACCT_ID);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6093(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {

			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String ThirdCustId = arryMessage[0];
			String TotalBalance = arryMessage[1];
			String TotalFreezeAmount = arryMessage[2];
			String Reserve = arryMessage[3];

			retKeyDict.put("ThirdCustId", ThirdCustId);
			retKeyDict.put("TotalBalance", TotalBalance);
			retKeyDict.put("TotalFreezeAmount", TotalFreezeAmount);
			retKeyDict.put("Reserve", Reserve);
		}
	}

	private void spiltMessage_6079(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 2 + 4];

			String[] array = ArrayContent.split("&");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (int i = 0, j = 0; j < iCount; i = i + 12, j++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("TranFlag", array[i]);
				map.put("TranStatus", array[i + 1]);
				map.put("ThirdCustId", array[i + 2]);
				map.put("CustAcctId", array[i + 3]);
				map.put("CustAcctName", array[i + 4]);
				map.put("TranAmount", array[i + 5]);
				map.put("TranDate", array[i + 6]);
				map.put("TranTime", array[i + 7]);
				map.put("FrontLogNo", array[i + 8]);
				map.put("ThirdLogNo", array[i + 9]);
				// map.put("Note", array[i + 10]);
				// map.put("Note2", array[i + 11]);
				list.add(map);
			}

			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", list);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6080(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 2 + 4];

			String[] array = ArrayContent.split("&");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (int i = 0, j = 0; j < iCount; i = i + 17, j++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("TranFlag", array[i]);
				map.put("TranStatus", array[i + 1]);
				map.put("TranAmount", array[i + 2]);
				map.put("HandFee", array[i + 3]);
				map.put("TranDate", array[i + 4]);
				map.put("TranTime", array[i + 5]);
				map.put("FrontLogNo", array[i + 6]);
				map.put("ThirdLogNo", array[i + 7]);
				map.put("ThirdHtId", array[i + 8]);
				map.put("OutCustAcctId", array[i + 9]);
				map.put("OutThirdCustId", array[i + 10]);
				map.put("OutCustAcctName", array[i + 11]);
				map.put("InCustAcctId", array[i + 12]);
				map.put("InThirdCustId", array[i + 13]);
				map.put("InCustAcctName", array[i + 14]);
				list.add(map);
			}

			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", list);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6085(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String FrontLogNo = arryMessage[0];
			String Reserve = arryMessage[1];

			retKeyDict.put("FrontLogNo", FrontLogNo);
			retKeyDict.put("Reserve", Reserve);
		}
	}

	private void spiltMessage_6084(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String Reserve = arryMessage[0];

			retKeyDict.put("Reserve", Reserve);
		}
	}

	private void spiltMessage_6083(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String RevMobilePhone = arryMessage[0];
			String SerialNo = arryMessage[1];
			String Reserve = arryMessage[2];

			retKeyDict.put("RevMobilePhone", RevMobilePhone);
			retKeyDict.put("SerialNo", SerialNo);
			retKeyDict.put("Reserve", Reserve);
		}
	}

	private void spiltMessage_6082(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String RevMobilePhone = arryMessage[0];
			String SerialNo = arryMessage[1];
			String Reserve = arryMessage[2];

			retKeyDict.put("RevMobilePhone", RevMobilePhone);
			retKeyDict.put("SerialNo", SerialNo);
			retKeyDict.put("Reserve", Reserve);
		}
	}

	private void spiltMessage_6089(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String FrontLogno = arryMessage[0];
			String reserve = arryMessage[1];

			retKeyDict.put("FrontLogNo", FrontLogno);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6000(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String custAcctId = arryMessage[0];
			String reserve = arryMessage[1];

			retKeyDict.put("CustAcctId", custAcctId);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_Reserve(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String reserve = arryMessage[0];

			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_FrontLogNo(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String FrontLogNo = arryMessage[0];
			String reserve = arryMessage[1];

			retKeyDict.put("FrontLogNo", FrontLogNo);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_HandFee(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String FrontLogNo = arryMessage[0];
			String HandFee = arryMessage[1];
			String reserve = arryMessage[2];

			retKeyDict.put("FrontLogNo", FrontLogNo);
			retKeyDict.put("HandFee", HandFee);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6014(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TranFlag = arryMessage[0];
			String TranStatus = arryMessage[1];
			String TranAmount = arryMessage[2];
			String TranDate = arryMessage[3];
			String TranTime = arryMessage[4];
			String InCustAcctId = arryMessage[5];
			String OutCustAcctId = arryMessage[6];
			String reserve = arryMessage[7];

			retKeyDict.put("TranFlag", TranFlag);
			retKeyDict.put("TranStatus", TranStatus);
			retKeyDict.put("TranAmount", TranAmount);
			retKeyDict.put("TranDate", TranDate);
			retKeyDict.put("TranTime", TranTime);
			retKeyDict.put("InCustAcctId", InCustAcctId);
			retKeyDict.put("OutCustAcctId", OutCustAcctId);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6027(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			int iCount = Integer.parseInt(TotalCount);
			int iBegin = bodyMessage.indexOf("&");
			// System.out.println(iBegin);
			int iEnd1 = bodyMessage.lastIndexOf("&");
			// System.out.println(iEnd1);
			String ArrayContent = bodyMessage.substring(iBegin + 1, iEnd1);
			// System.out.println(ArrayContent);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			// System.out.println(iEnd2);
			ArrayContent = ArrayContent.substring(0, iEnd2);
			int iEnd3 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd3);
			// System.out.println(ArrayContent);
			String reserve = arryMessage[iCount * 2 + 1];
			String reserve2 = arryMessage[iCount * 2 + 2];

			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
			retKeyDict.put("Reserve2", reserve2);
		}
	}

	private void spiltMessage_6048(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			int iCount = Integer.parseInt(TotalCount);
			int iBegin = bodyMessage.indexOf("&");
			int iEnd1 = bodyMessage.lastIndexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin + 1, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 2 + 1];

			String[] array = ArrayContent.split("&");
			if (array != null && array.length > 0) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (int i = 0, j = 0; j < iCount; i = i + 5, j++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("FrontLogNo", array[i]);
					map.put("ThirdLogNo", array[i + 1]);
					map.put("Remark", array[i + 2]);
					map.put("WithDrawRemark", array[i + 3]);
					map.put("WithDrawDate", array[i + 4]);
					list.add(map);
				}
			}
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6040(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 5 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6041(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 9 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6042(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TranType = arryMessage[0];
			String TranStatus = arryMessage[1];
			String TranAmount = arryMessage[2];
			String TranDate = arryMessage[3];
			String TranTime = arryMessage[4];
			String reserve = arryMessage[5];

			retKeyDict.put("TranType", TranType);
			retKeyDict.put("TranStatus", TranStatus);
			retKeyDict.put("TranAmount", TranAmount);
			retKeyDict.put("TranDate", TranDate);
			retKeyDict.put("TranTime", TranTime);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6043(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String Balance = arryMessage[0];
			String TotalIncome = arryMessage[1];
			String WeekIncome = arryMessage[2];
			String LastIncome = arryMessage[3];
			String reserve = arryMessage[4];

			retKeyDict.put("Balance", Balance);
			retKeyDict.put("TotalIncome", TotalIncome);
			retKeyDict.put("WeekIncome", WeekIncome);
			retKeyDict.put("LastIncome", LastIncome);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6044(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 6 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6050(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 2 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6072(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 10 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6073(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 12 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6010(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalCount = arryMessage[0];
			String BeginNum = arryMessage[1];
			String LastPage = arryMessage[2];
			String RecordNum = arryMessage[3];
			int iCount = Integer.parseInt(RecordNum);
			int iBegin1 = bodyMessage.indexOf("&");
			String ArrayContent = bodyMessage.substring(iBegin1 + 1);
			int iBegin2 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin2 + 1);
			int iBegin3 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin3 + 1);
			int iBegin4 = ArrayContent.indexOf("&");
			ArrayContent = ArrayContent.substring(iBegin4 + 1);
			int iEnd1 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd1);
			int iEnd2 = ArrayContent.lastIndexOf("&");
			ArrayContent = ArrayContent.substring(0, iEnd2);
			String reserve = arryMessage[iCount * 7 + 4];
			retKeyDict.put("TotalCount", TotalCount);
			retKeyDict.put("BeginNum", BeginNum);
			retKeyDict.put("LastPage", LastPage);
			retKeyDict.put("RecordNum", RecordNum);
			retKeyDict.put("ArrayContent", ArrayContent);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6011(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String LastBalance = arryMessage[0];
			String CurBalance = arryMessage[1];
			String reserve = arryMessage[2];

			retKeyDict.put("LastBalance", LastBalance);
			retKeyDict.put("CurBalance", CurBalance);
			retKeyDict.put("Reserve", reserve);
		}
	}

	private void spiltMessage_6037(HashMap retKeyDict) {
		if (retKeyDict.containsKey("BodyMsg")) {
			String bodyMessage = (String) retKeyDict.get("BodyMsg");
			String[] arryMessage = bodyMessage.split("&");
			String TotalAmount = arryMessage[1];
			String TotalBalance = arryMessage[2];
			String TotalFreezeAmount = arryMessage[3];
			String custAcctId = arryMessage[0];
			String reserve = arryMessage[4];

			retKeyDict.put("CustAcctId", custAcctId);
			retKeyDict.put("TotalAmount", TotalAmount);
			retKeyDict.put("TotalBalance", TotalBalance);
			retKeyDict.put("TotalFreezeAmount", TotalFreezeAmount);
			retKeyDict.put("Reserve", reserve);
		}
	}

}
