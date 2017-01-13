package com.ryel.zaja.controller.api;

import com.ecc.emp.data.KeyedCollection;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.pingan.WalletConstant;
import com.ryel.zaja.utils.JsonUtil;
import com.ryel.zaja.utils.VerifyCodeUtil;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
            String masterId = WalletConstant.SUP_ACCT_ID;

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
            try {
                VerifyCodeUtil.send("13554372007","[orig]"+orig,"1");
                VerifyCodeUtil.send("13554372007","[sign]"+sign,"1");
            }catch (Exception e){
                e.printStackTrace();
            }


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
            String payStatus = (String) output.getDataValue("status");
            if (StringUtils.equals("01", payStatus)) {
                String orderId = (String) output.getDataValue("orderId");
            } else {
                String errorMsg = (String) output.getDataValue("errorMsg");
                logger.info("失败原因====================" + errorMsg);
            }

            try {
                VerifyCodeUtil.send("13554372007","[output]"+JsonUtil.obj2Json(output),"1");
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "opened")
    public void opened(String orig, String sign) {
        try {
            try {
                VerifyCodeUtil.send("13554372007","[orig]"+orig,"1");
                VerifyCodeUtil.send("13554372007","[sign]"+sign,"1");
            }catch (Exception e){
                e.printStackTrace();
            }


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
            String payStatus = (String) output.getDataValue("status");
            if (StringUtils.equals("01", payStatus)) {
                String orderId = (String) output.getDataValue("orderId");
            } else {
                String errorMsg = (String) output.getDataValue("errorMsg");
                logger.info("失败原因====================" + errorMsg);
            }

            try {
                VerifyCodeUtil.send("13554372007","[output]"+JsonUtil.obj2Json(output),"1");
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

