package com.ryel.zaja.controller.api;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.PinanBankCodeConfig;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.HouseOrderStatus;
import com.ryel.zaja.core.exception.BizException;
import com.ryel.zaja.entity.HouseOrder;
import com.ryel.zaja.entity.PinganOrder;
import com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo;
import com.ryel.zaja.controller.api.pingan.WalletConstant;
import com.ryel.zaja.pingan.PinganUtils;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.PinganOrderService;
import com.ryel.zaja.service.ZjjzCnapsBankinfoService;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController()
@RequestMapping(value = "/api/pingan/", produces = "application/json; charset=UTF-8")
public class PinganApi {
    protected final static Logger logger = LoggerFactory.getLogger(PinganApi.class);


    @Autowired
    private PinganOrderService pinganOrderService;
    @Autowired
    private HouseOrderService houseOrderService;
    @Autowired
    private ZjjzCnapsBankinfoService zjjzCnapsBankinfoService;
    @Autowired
    private WalletConstant wallet;


    /**
     * 通过userId 进行开卡前的加密处理
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "quickpaymentparamencrypt")
    public Result quickpaymentparamencrypt(String userId) {
        try {

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
            input.put("orderId", PinganUtils.getOderId(masterId, datetamp));//订单号，严格遵守格式：商户号+8位日期YYYYMMDD+8位流水
            input.put("customerId", userId);//会员号
            input.put("dateTime", timestamp);//下单时间，YYYYMMDDHHMMSS


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

            return Result.success().data(param);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
    }


    /**
     * 快捷支付开卡查询
     */
    @RequestMapping(value = "bankopened")
    public Result UnionAPI_Opened(Integer userId) {

        try {
            PayclientInterfaceUtil util = new PayclientInterfaceUtil();

            com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
            com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

            input.put("masterId", WalletConstant.QUICK_PAYMENT_ID);
            input.put("customerId", userId);

            output = util.execute(input, "UnionAPI_Opened");

            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

            if ((errorCode == null || errorCode.replaceAll(" ", "").equals("") || errorCode.equals("")) && (errorMsg == null || errorCode.replaceAll(" ", "").equals("") || errorMsg.equals(""))) {
                IndexedCollection icoll = (IndexedCollection) output.getDataElement("unionInfo");

                List<Map> list = new ArrayList<>();

                for (int i = 0; i < icoll.size(); i++) {
                    com.ecc.emp.data.KeyedCollection kcoll = (com.ecc.emp.data.KeyedCollection) icoll.getElementAt(i);
                    Map bank = new HashMap<String, String>();
                    bank.put("openId", (String) kcoll.getDataValue("OpenId"));
                    bank.put("accNo", (String) kcoll.getDataValue("accNo"));
                    bank.put("telephone", (String) kcoll.getDataValue("telephone"));
                    bank.put("plantBank", PinanBankCodeConfig.getBank((String) kcoll.getDataValue("plantBankId")));
                    bank.put("bankType", ((String) kcoll.getDataValue("bankType")).equals("01") ? "借记卡" : "信用卡");
                    list.add(bank);
                }
                Map data = new HashMap<String, String>();
                data.put("list", list);
                return Result.success().msg("").data(data);
            } else {
                logger.error("银行查询报错", errorMsg);
                return Result.error().msg(pinganError(errorMsg)).data(new HashMap<>());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
    }

    /**
     * 发起银行卡关闭（UnionAPI_OPNCL）
     *
     * @param userId 用户id
     * @param number 需要关闭的数量，从userid向上递增
     * @return
     */
    @RequestMapping(value = "bankclose")
    public Result UnionAPI_OPNCL(Integer userId, Integer number) {

        try {

            number = number == null ? 1 : number;

            Map data = new HashMap<String, String>();
            Map errData = new HashMap<String, String>();

            //最后返回的map
            Map resultMap = new HashMap<String, ObjectFactory>();

            for (int i = 0; i < number; i++) {
                Integer lastId = userId + i;
                Result result = UnionAPI_Opened(lastId);
                if (result.getStatus() != 1) {

                    ArrayList resuList = (ArrayList) ((HashMap) result.getData()).get("list");

                    for (int k = 0; k < resuList.size(); k++) {
                        PayclientInterfaceUtil util = new PayclientInterfaceUtil();

                        com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
                        com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

                        input.put("masterId", WalletConstant.QUICK_PAYMENT_ID);
                        input.put("customerId", lastId);
                        input.put("OpenId", ((HashMap) resuList.get(k)).get("openId"));

                        output = util.execute(input, "UnionAPI_OPNCL");

                        String errorCode = (String) output.getDataValue("errorCode");
                        String errorMsg = (String) output.getDataValue("errorMsg");
                        String masterId = (String) output.getDataValue("masterId");
                        String customerId = (String) output.getDataValue("customerId");
                        String status = (String) output.getDataValue("status");
                        String openId = (String) output.getDataValue("OpenId");

                        if (status.equals("01")) {
                            Map bank = new HashMap<String, String>();
                            bank.put("masterId", masterId);
                            bank.put("openId", openId);
                            bank.put("customerId", customerId);
                            bank.put("telephone", (String) output.getDataValue("telephone"));
                            bank.put("accNo", (String) output.getDataValue("accNo"));


                            data.put(lastId + "-" + (k+1), bank);
                        } else {
                            Map errMap = new HashMap<String, String>();
                            errMap.put("errorCode", errorCode);
                            errMap.put("errorMsg", errorMsg);
                            errMap.put("masterId", masterId);
                            errMap.put("customerId", customerId);
                            errMap.put("openId", openId);

                            errData.put(lastId + "-" + (k+1), errMap);
                        }
                    }
                } else {
                    continue;
                }
            }
            resultMap.put("data", data);
            resultMap.put("errData", errData);

            return Result.success().msg("").data(resultMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
    }

    /**
     * 发起单张银行卡关闭
     */
    @RequestMapping(value = "bankclose2", method = RequestMethod.POST)
    public Result UnionAPI_OPNCL2(Integer userId, String OpenId){
        try {
            Map data = new HashMap<String, String>();

            PayclientInterfaceUtil util = new PayclientInterfaceUtil();

            com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
            com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

            input.put("masterId", WalletConstant.QUICK_PAYMENT_ID);
            input.put("customerId", userId);
            input.put("OpenId", OpenId);

            output = util.execute(input, "UnionAPI_OPNCL");

            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");
            String masterId = (String) output.getDataValue("masterId");
            String customerId = (String) output.getDataValue("customerId");
            String status = (String) output.getDataValue("status");
            String openId = (String) output.getDataValue("OpenId");

            if (status.equals("01")) {

                data.put("masterId", masterId);
                data.put("openId", openId);
                data.put("customerId", customerId);
                data.put("telephone", (String) output.getDataValue("telephone"));
                data.put("accNo", (String) output.getDataValue("accNo"));


                return Result.success().msg("").data(data);
            } else {
                data.put("errorCode", errorCode);
                data.put("errorMsg", errorMsg);
                data.put("masterId", masterId);
                data.put("customerId", customerId);
                data.put("openId", openId);

                return Result.error().msg(pinganError(errorMsg)).data(data);
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
    }

    /**
     * 入金钱发生短信验证码
     */
    @RequestMapping(value = "ordersms")
    public Result UnionAPI_SSMS(Integer userId, String openId, String amount) {
        try {
            PayclientInterfaceUtil util = new PayclientInterfaceUtil();
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
            String orderId = PinganUtils.getOderId(masterId, datetamp);
            input.put("masterId", masterId);
            input.put("customerId", userId);
            input.put("orderId", orderId);
            input.put("currency", "RMB");
            input.put("OpenId", openId);
            input.put("amount", amount);
            input.put("paydate", timestamp);

            output = util.execute(input, "UnionAPI_SSMS");


            System.out.println("---output---" + output);


            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

            if ((errorCode == null || errorCode.replaceAll(" ", "").equals("")) && (errorMsg == null || errorCode.replaceAll(" ", "").equals(""))) {
                // 短信发送成功

                Map pay = new HashMap<>();
                pay.put("pinganOrderId", output.getDataValue("orderId"));
                pay.put("amount", output.getDataValue("amount"));
                pay.put("paydate", output.getDataValue("paydate"));

                Map data = new HashMap<>();
                data.put("paymodel", pay);

                return Result.success().msg("").data(data);
            } else if(errorCode.equals("UKHPY37")){
                return Result.error().msg(Error_code.ERROR_CODE_0046).data(new HashMap<>());
            }else{
                logger.error("支付短信报错" + errorMsg);
                return Result.error().msg(pinganError(errorMsg)).data(new HashMap<>());
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
    }


    /**
     * 后台发出佣金的交易申请
     *
     * @param fromUserId     from会员号
     * @param toUserId     to会员号
     * @param openId     银行卡号
     * @param amount     价格
     * @param orderId    订单号
     * @param paydate    支付时间
     * @param verifyCode 短信验证码
     */
    @RequestMapping(value = "commissionsubmit")
    public Result UnionAPI_Submit(Integer fromUserId,Integer toUserId, String openId, String amount, Integer orderId, String pinganOrderId, String paydate, String verifyCode) {
        try {


            //判断是不是交易中
            Boolean isTransaction = houseOrderService.checkHouseInTransaction(orderId);

            if (isTransaction)
            {
                return Result.error().msg(Error_code.ERROR_CODE_0042).data(new HashMap<>());
            }


            PayclientInterfaceUtil util = new PayclientInterfaceUtil();

            com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
            com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

            String masterId = WalletConstant.QUICK_PAYMENT_ID;
            input.put("masterId", masterId);
            input.put("orderId", pinganOrderId);
            input.put("currency", "RMB");
            input.put("amount", amount);
            input.put("objectName", "Pay Commission to agent");
            input.put("paydate", paydate);
            input.put("validtime", "0");//订单有效期(毫秒)，0不生效
            input.put("remark", orderId);
            input.put("customerId", fromUserId);
            input.put("OpenId", openId);
            input.put("NOTIFYURL", "https://zaja.xin/zaja/api/pingan/notify/commissionnotify");
            input.put("verifyCode", verifyCode);// 短信验证码

            output = util.execute(input, "UnionAPI_Submit"); //执行发送，并返回结果对象


            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");
            String status = (String) output.getDataValue("status");

            if ((errorCode == null || errorCode.replaceAll(" ", "").equals("")) && (errorMsg == null || errorCode.replaceAll(" ", "").equals(""))) {

                return UnionAPI_OrderQuery(fromUserId,toUserId,(String)output.getDataValue("accNo"),(String)output.getDataValue("telephone"),orderId,pinganOrderId);
            } else {
                logger.error("支付交易报错" + errorMsg);
                return Result.error().msg(pinganError(errorMsg)).data(new HashMap<>());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
    }

    /**
     * 支付交易查询接口
     * @param fromUserId
     * @param toUserId
     * @param accNo
     * @param telephone
     * @param orderId
     * @param pinganOrderId
     * @return
     */
    @RequestMapping(value = "orderquery")
    public Result UnionAPI_OrderQuery(Integer fromUserId,Integer toUserId,String accNo,String telephone, Integer orderId,String pinganOrderId) {
        try {
            PayclientInterfaceUtil util = new PayclientInterfaceUtil();

            com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
            com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

            String masterId = WalletConstant.QUICK_PAYMENT_ID;
            input.put("masterId", masterId);
            input.put("orderId", pinganOrderId);
            input.put("customerId", fromUserId);


            output = util.execute(input, "UnionAPI_OrderQuery"); //执行发送，并返回结果对象


            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

            String status = (String) output.getDataValue("status");

            if (status.equals("01")) {

                PinganOrder order = new PinganOrder();
                order.setMasterId((String) output.getDataValue("masterId"));
                order.setPinganOrderId((String) output.getDataValue("orderId"));
                order.setAmount((String) output.getDataValue("amount"));
                order.setCharge((String) output.getDataValue("charge"));
                order.setValidtime((String) output.getDataValue("validtime"));
                order.setCustomerId(fromUserId+"");
                order.setAccNo(accNo);
                order.setMobile(telephone);
                order.setOrderId(orderId);
                order.setObjectName((String) output.getDataValue("objectName"));
                order.setCurrency((String) output.getDataValue("currency"));
                order.setPayTime(pinganTimeToDate((String) output.getDataValue("date")));
                order.setOrderTime(pinganTimeToDate((String) output.getDataValue("paydate")));

                houseOrderService.payment(Integer.parseInt(order.getCustomerId()), order.getOrderId());
                pinganOrderService.create(order);

                //资金进入会员子账户 和 冻结资金到担保账户
                wallet.frozennMoney("3",fromUserId,toUserId,(String) output.getDataValue("amount"),orderId);

                return Result.success().msg("").data(new HashMap<>());
            } else {
                logger.error("支付交易查询报错" + errorMsg);
                return Result.error().msg(pinganError(errorMsg)).data(new HashMap<>());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }
    }


    /**
     * 将长时间格式字符串转换为时间 yyyyMMddHHmmss
     *
     * @param pinantime
     * @return
     */
    public Date pinganTimeToDate(String pinantime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = formatter.parse(pinantime);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 去掉错误信息的[]
     * @param msg
     * @return
     */
    public String pinganError(String msg)
    {
//        int left = msg.indexOf("[");
//        int right = msg.indexOf("]");
//        String replace = msg.substring(left,right);
//        return  msg.replace(replace,"");

        return msg;

    }



    /**
     * 通过银行代码城市代码查询bankno,bankname
     * @param bankclscode
     * @param citycode
     * @return
     */
    @RequestMapping(value = "queryBanknameAndNo", method = RequestMethod.POST)
    public Result queryBanknameAndNo(String bankclscode, String citycode,String bankname){

        try{
            Map<String, Object> data = new HashMap<String, Object>();
            List<ZjjzCnapsBankinfoVo> list;
            if(null != bankname){
                list = zjjzCnapsBankinfoService.findByBankclscodeAndCitycodeAndBankname(bankclscode, citycode, bankname);

            }else {
                list = zjjzCnapsBankinfoService.findByBankclscodeAndCitycode(bankclscode, citycode);
            }
            data.put("list", list);
            return Result.success().msg("").data(data);
        }catch (Exception e){
            return Result.error().msg(Error_code.ERROR_CODE_0043).data(new HashMap<>());
        }

    }

}

