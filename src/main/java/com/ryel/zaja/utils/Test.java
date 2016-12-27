package com.ryel.zaja.utils;

import com.ryel.zaja.pingan.WalletConstant;
import com.sdb.payclient.bean.exception.CsiiException;

import java.util.HashMap;
import java.util.Map;

/**
 * Test
 *
 * @author leilei.gao
 * @date 2016/12/27 14:42
 */
public class Test {
    public static void main(String[] args){
        String userId = "test";
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

        com.sdb.payclient.core.PayclientInterfaceUtil util = null;
        try {
            util = new com.sdb.payclient.core.PayclientInterfaceUtil();
        } catch (CsiiException e) {
            e.printStackTrace();
        }
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

        System.out.println("--orig----" + orig);
        System.out.println("--sign----" + sign);
        String resp = HttpsRequestUtil.httpRequest("https://testebank.sdb.com.cn/khpayment/UnionAPI_Open.do?orig="+orig+"&sign="+sign,"POST",null);
        System.out.println("--resp----" + resp);
    }
}
