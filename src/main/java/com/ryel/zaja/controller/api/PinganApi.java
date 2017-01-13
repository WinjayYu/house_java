package com.ryel.zaja.controller.api;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.pingan.WalletConstant;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.VerifyCodeUtil;
import com.sdb.payclient.bean.exception.CsiiException;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping(value = "/api/pingan/",produces = "application/json; charset=UTF-8")
public class PinganApi {
    protected final static Logger logger = LoggerFactory.getLogger(PinganApi.class);

    @RequestMapping(value = "quickpaymentparamencrypt")
    public Result quickpaymentparamencrypt(String userId) {
        try {
            logger.info("接口(quickpaymentparamencrypt)入参：userId=" + userId);
            int orderid;// '订单号
            String orderids;
            java.util.Random r = new java.util.Random();
            while (true) {
                orderid = r.nextInt(99999999);
                if (orderid < 0)
                    orderid = -orderid;
                orderids = String.valueOf(orderid);

                if (orderids.length() < 8) {
                    continue;
                }
                if (orderids.length() >= 8) {
                    orderids = orderids.substring(0, 8);
                    break;
                }
            }

            System.out.println("--orderids----" + orderids);

            String timestamp;
            String datetamp;
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            java.util.Date date = calendar.getTime();
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
            timestamp = formatter.format(date);
            datetamp = timestamp.substring(0, 8);
            String masterId = WalletConstant.QUICK_PAYMENT_ID;

            com.sdb.payclient.core.PayclientInterfaceUtil util = new com.sdb.payclient.core.PayclientInterfaceUtil();
            com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
            com.ecc.emp.data.KeyedCollection signDataput = new com.ecc.emp.data.KeyedCollection("signDataput");

            input.put("masterId", masterId);//商户号，注意生产环境上要替换成商户自己的生产商户号
            input.put("orderId", masterId + datetamp + orderids);//订单号，严格遵守格式：商户号+8位日期YYYYMMDD+8位流水
            input.put("customerId", userId);//会员号
            input.put("dateTime", timestamp);//下单时间，YYYYMMDDHHMMSS

            System.out.println("--masterId----" + masterId);
            System.out.println("--orderId----" + masterId + datetamp + orderids);
            System.out.println("--customerId----" + userId);
            System.out.println("--dateTime----" + timestamp);

            String orig = "";        //原始数据
            String origData = "";
            String sign = "";        //产生签名
            String encoding = "GBK";
            try {//发送前，得到签名数据和签名后数据，单独使用
                signDataput = util.getSignData(input);
                System.out.println("--signDataput----" + signDataput.toString());
                orig = (String) signDataput.getDataValue("orig");
                origData = orig.replace("\t", "");
                System.out.println(origData);
                sign = (String) signDataput.getDataValue("sign");
                orig = util.Base64Encode(orig, encoding);
                sign = util.Base64Encode(sign, encoding);
                orig = java.net.URLEncoder.encode(orig, encoding);
                sign = java.net.URLEncoder.encode(sign, encoding);
            } catch (Exception e1) {
                e1.printStackTrace();
                orig = e1.getMessage();
            }
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("orig", orig);
            param.put("sign", sign);
            System.out.println("--orig----" + orig);
            System.out.println("--sign----" + sign);
            logger.info("出参：" + JsonUtil.obj2Json(param));
            return Result.success().data(param);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    @RequestMapping(value = "paynotify")
    public void paynotify(String orig, String sign) {
        try {

            PayclientInterfaceUtil util = new PayclientInterfaceUtil();
            KeyedCollection output = new KeyedCollection("output");

            String encoding = "GBK";
            logger.info("---银行返回后台通知原始数据---" + orig);
            logger.info("---银行返回后台通知签名数据---" + sign);

            orig = PayclientInterfaceUtil.Base64Decode(orig, encoding);
            sign = PayclientInterfaceUtil.Base64Decode(sign, encoding);
            logger.info("---Base64Decode后的后台通知原始数据---" + orig);
            logger.info("---Base64Decode后的后台通知签名数据---" + sign);

            boolean result = util.verifyData(sign, orig);
            logger.info("---通知验签结果---" + result);
            if (!result) {
                logger.info("---验签失败---" + result);
            }

//            output = util.parseOrigData(orig);
//            logger.info("---平安订单详细信息---" + output);
//            logger.info("---平安订单详细信息---" + JsonUtil.obj2Json(output));
//            String payStatus = (String) output.getDataValue("status");
//            if (StringUtils.equals("01", payStatus)) {
//                String orderId = (String) output.getDataValue("orderId");
//            } else {
//                String errorMsg = (String) output.getDataValue("errorMsg");
//                logger.info("失败原因====================" + errorMsg);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "opened")
    public void opened(String orig, String sign) {
        try {

            PayclientInterfaceUtil util = new PayclientInterfaceUtil();
            KeyedCollection output = new KeyedCollection("output");

            String encoding = "GBK";
            logger.info("---银行返回后台通知原始数据---" + orig);
            logger.info("---银行返回后台通知签名数据---" + sign);

            orig = PayclientInterfaceUtil.Base64Decode(orig, encoding);
            sign = PayclientInterfaceUtil.Base64Decode(sign, encoding);
            logger.info("---Base64Decode后的后台通知原始数据---" + orig);
            logger.info("---Base64Decode后的后台通知签名数据---" + sign);

            boolean result = util.verifyData(sign, orig);
            logger.info("---通知验签结果---" + result);
            if (!result) {
                logger.info("---验签失败---" + result);
            }

            output = util.parseOrigData(orig);
            logger.info("---平安订单详细信息---" + output);
            logger.info("---平安订单详细信息---" + JsonUtil.obj2Json(output));
            // output包含如下信息：
//                errorCode	varchar	8	错误返回相应的错误码, 正常返回为空
//                errorMsg	varchar	100	错误码对应的错误说明，正常返回为空
//                status	char	2	01为成功，02为失败
//                masterId	char	10	商户号
//                orderId	varchar	26	订单号
//                dateTime	varchar	14	交易时间，YYYYMMDDHHMMSS
//                customerId	varchar	30	商户会员号
            String payStatus = (String) output.getDataValue("status");
            if (StringUtils.equals("01", payStatus)) {
                // 开卡成功
                logger.info("开卡成功（快捷支付）====================");
            } else {
                String errorMsg = (String) output.getDataValue("errorMsg");
                logger.info("开卡失败（快捷支付）：" + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 快捷支付开卡查询
     */
    @RequestMapping(value = "unionapiopened")
    public void UnionAPI_Opened(Integer userId) {
        PayclientInterfaceUtil  util = null;
        try {
            util = new PayclientInterfaceUtil();
        } catch (CsiiException e) {
            e.printStackTrace();
        }
        com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
        com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

        input.put("masterId",WalletConstant.QUICK_PAYMENT_ID);
        input.put("customerId",userId);

        try {
            output = util.execute(input,"UnionAPI_Opened");
            System.out.println("---output---"+output);
            System.out.println("---masterId---"+output.getDataValue("masterId"));
            System.out.println("---customerId---"+output.getDataValue("customerId"));
            IndexedCollection icoll = (IndexedCollection) output.getDataElement("unionInfo") ;
//            unionInfo	商户已开通银行列表（以下字段属于集合内字段）
//            OpenId	varchar	26	银行卡开通ID
//            accNo	varchar	4	银行卡号后四位
//            bankType	varchar	2	01是借记卡，02是信用卡
//            telephone	varchar	20	银行预留手机号
//            plantId	varchar	10	支付平台ID
//            plantBankId	varchar	20	支付平台银行ID

            String OpenId=null;
            String accNo=null;
            String telephone=null;
            String plantId=null;
            String plantBankId=null;
            String bankType=null;
            for( int i=0; i<icoll.size(); i++){
                com.ecc.emp.data.KeyedCollection kcoll = (com.ecc.emp.data.KeyedCollection)icoll.getElementAt(i);
                OpenId = (String)kcoll.getDataValue("OpenId");
                accNo = (String)kcoll.getDataValue("accNo");
                telephone = (String)kcoll.getDataValue("telephone");
                plantId = (String)kcoll.getDataValue("plantId");
                plantBankId = (String)kcoll.getDataValue("plantBankId");
                bankType = (String)kcoll.getDataValue("bankType");
            }

            System.out.println("---OpenIdd---"+OpenId);
            System.out.println("---accNo---"+accNo);
            System.out.println("---telephone---"+telephone);
            System.out.println("---plantId---"+plantId);
            System.out.println("---plantBankId---"+plantBankId);
            System.out.println("---bankType---"+bankType);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 入金钱发生短信验证码
     */
    @RequestMapping(value = "unionapissms")
    public void UnionAPI_SSMS(Integer userId,String openId,String amount) {
        PayclientInterfaceUtil util = null;
        try {
            util = new PayclientInterfaceUtil();
        } catch (CsiiException e) {
            e.printStackTrace();
        }
        com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
        com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");
        String timestamp;
        String datetamp;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        timestamp = formatter.format(date);
        datetamp = timestamp.substring(0, 8);

        String masterId = WalletConstant.QUICK_PAYMENT_ID;
        String orderId = getOderId(masterId, datetamp);
        input.put("masterId", masterId);
        input.put("customerId", userId);
        input.put("orderId", orderId);
        input.put("currency", "RMB");
        input.put("OpenId", openId);
        input.put("amount", amount);
        input.put("paydate", timestamp);

        try {
            output = util.execute(input, "UnionAPI_SSMS");
            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

            System.out.println("---output---" + output);

            if ((errorCode == null || errorCode.equals("")) && (errorMsg == null || errorMsg.equals(""))) {
                // 短信发送成功
                System.out.println("---masterId---" + output.getDataValue("masterId"));
                System.out.println("---customerId---" + output.getDataValue("customerId"));
                System.out.println("---orderId---" + output.getDataValue("orderId"));
                System.out.println("---currency---" + output.getDataValue("currency"));
                System.out.println("---amount---" + output.getDataValue("amount"));
                System.out.println("---status̬---" + output.getDataValue("status"));
                System.out.println("---paydate---" + output.getDataValue("paydate"));
            } else {
                System.out.println("---errorCode---" + output.getDataValue("errorCode"));
                System.out.println("---errorMsg---" + output.getDataValue("errorMsg"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getOderId(String masterId, String datetamp) {
        int orderid;
        String orderids;
        java.util.Random r = new java.util.Random();
        while (true) {
            orderid = r.nextInt(99999999);
            if (orderid < 0)
                orderid = -orderid;
            orderids = String.valueOf(orderid);
            System.out.println("--orderids----" + orderids);
            if (orderids.length() < 8) {
                System.out.println("--order22222ids----" + orderids);
                continue;
            }
            if (orderids.length() >= 8) {
                orderids = orderids.substring(0, 8);
                System.out.println("--orderids222----" + orderids);
                break;
            }
        }
        return masterId + datetamp + orderids;
    }

    /**
     * 发起后台支付交易
     */
    @RequestMapping(value = "unionapisubmit")
    public void UnionAPI_Submit(Integer userId, String openId, String amount) {
        String businessCode = "UnionAPI_Submit";
        PayclientInterfaceUtil util = null;
        try {
            util = new PayclientInterfaceUtil();
        } catch (CsiiException e) {
            e.printStackTrace();
        }
        com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
        com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

        String timestamp;
        String datetamp;
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        timestamp = formatter.format(date);
        datetamp = timestamp.substring(0, 8);
        String masterId = WalletConstant.QUICK_PAYMENT_ID;
        input.put("masterId", masterId);
        String orderId = getOderId(masterId, datetamp);
        input.put("orderId", orderId);
        input.put("currency", "RMB");
        input.put("amount", amount);
        input.put("objectName", "unionpay01");
        input.put("paydate", timestamp);
        input.put("validtime", "0");//订单有效期(毫秒)，0不生效
        input.put("remark", "unionpay01 ");
        input.put("customerId", userId);
        input.put("OpenId", openId);
        input.put("NOTIFYURL", "https://testebank.sdb.com.cn/corporbank/unionpayNotify.jsp");
        input.put("verifyCode", "111111");// 短信验证码

        try {
            output = util.execute(input, businessCode); //执行发送，并返回结果对象
            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");
            String status = (String) output.getDataValue("status");
            String orderIdf = (String) output.getDataValue("orderId");
            String paydatef = (String) output.getDataValue("paydate");
            if (status.equals("01")) {

            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}

