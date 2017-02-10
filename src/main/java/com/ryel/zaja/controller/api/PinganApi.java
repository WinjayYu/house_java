package com.ryel.zaja.controller.api;

import com.ecc.emp.data.IndexedCollection;
import com.ecc.emp.data.KeyedCollection;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.PinanBankCodeConfig;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.config.enums.PinganApiEnum;
import com.ryel.zaja.config.enums.TradeRecordStatus;
import com.ryel.zaja.entity.PinanOrder;
import com.ryel.zaja.entity.ZjjzCnapsBankinfo;
import com.ryel.zaja.entity.TradeRecord;
import com.ryel.zaja.entity.User;
import com.ryel.zaja.entity.UserWalletAccount;
import com.ryel.zaja.pingan.PinganUtils;
import com.ryel.zaja.entity.vo.ZjjzCnapsBankinfoVo;
import com.ryel.zaja.pingan.WalletConstant;
import com.ryel.zaja.pingan.ZJJZ_API_GW;
import com.ryel.zaja.service.HouseOrderService;
import com.ryel.zaja.service.HouseService;
import com.ryel.zaja.service.PinanOrderService;
import com.ryel.zaja.service.ZjjzCnapsBankinfoService;
import com.ryel.zaja.service.UserWalletAccountService;
import com.ryel.zaja.utils.JsonUtil;
import com.sdb.payclient.bean.exception.CsiiException;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController()
@RequestMapping(value = "/api/pingan/", produces = "application/json; charset=UTF-8")
public class PinganApi {
    protected final static Logger logger = LoggerFactory.getLogger(PinganApi.class);


    @Autowired
    private PinanOrderService pinanOrderService;
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
            input.put("orderId", getOderId(masterId, datetamp));//订单号，严格遵守格式：商户号+8位日期YYYYMMDD+8位流水
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
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }


    /**
     * 开卡成功回调
     *
     * @param orig
     * @param sign
     */
    @RequestMapping(value = "opencardback")
    public void openCardBack(String orig, String sign) {
        try {

            PayclientInterfaceUtil util = new PayclientInterfaceUtil();
            KeyedCollection output = new KeyedCollection("output");

            String encoding = "GBK";


            orig = PayclientInterfaceUtil.Base64Decode(orig, encoding);
            sign = PayclientInterfaceUtil.Base64Decode(sign, encoding);


            boolean result = util.verifyData(sign, orig);
            logger.info("---通知验签结果---" + result);
            if (!result) {
                logger.info("---验签失败---" + result);
                return;
            }

            output = util.parseOrigData(orig);

            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

            if ((errorCode == null || errorCode.replaceAll(" ", "").equals("")) && (errorMsg == null || errorCode.replaceAll(" ", "").equals(""))) {
                // 开卡成功
                logger.info("开卡成功（快捷支付）====================");
            } else {
                logger.info("开卡失败（快捷支付）：" + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

            if ((errorCode == null || errorCode.replaceAll(" ", "").equals("")) && (errorMsg == null || errorCode.replaceAll(" ", "").equals(""))) {
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
                return Result.error().msg(errorMsg).data(new HashMap<>());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
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
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
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
            String orderId = getOderId(masterId, datetamp);
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
            } else {

                return Result.error().msg(errorMsg).data(new HashMap<>());
            }


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
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
    public Result UnionAPI_Submit(Integer fromUserId,Integer toUserId, String openId, String amount, String orderId, String pinganOrderId, String paydate, String verifyCode) {
        try {
            PayclientInterfaceUtil util = new PayclientInterfaceUtil();

            com.ecc.emp.data.KeyedCollection input = new com.ecc.emp.data.KeyedCollection("input");
            com.ecc.emp.data.KeyedCollection output = new com.ecc.emp.data.KeyedCollection("output");

            String masterId = WalletConstant.QUICK_PAYMENT_ID;
            input.put("masterId", masterId);
            input.put("orderId", pinganOrderId);
            input.put("currency", "RMB");
            input.put("amount", amount);
            input.put("objectName", "Commission for agent");
            input.put("paydate", paydate);
            input.put("validtime", "0");//订单有效期(毫秒)，0不生效
            input.put("remark", orderId);
            input.put("customerId", fromUserId);
            input.put("OpenId", openId);
            input.put("NOTIFYURL", "https://zaja.xin/zaja/api/pingan/commissionnotify");
            input.put("verifyCode", verifyCode);// 短信验证码

            output = util.execute(input, "UnionAPI_Submit"); //执行发送，并返回结果对象


            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");


            if ((errorCode == null || errorCode.replaceAll(" ", "").equals("")) && (errorMsg == null || errorCode.replaceAll(" ", "").equals(""))) {

                PinanOrder order = new PinanOrder();
                order.setMasterId((String) output.getDataValue("masterId"));
                order.setOrderId((String) output.getDataValue("orderId"));
                order.setAmount((String) output.getDataValue("amount"));
                order.setCharge((String) output.getDataValue("charge"));
                order.setValidtime((String) output.getDataValue("validtime"));
                order.setCustomerId((String) output.getDataValue("customerId"));
                order.setAccNo((String) output.getDataValue("accNo"));
                order.setMobile((String) output.getDataValue("telephone"));
                order.setRemark((String) output.getDataValue("remark"));
                order.setObjectName((String) output.getDataValue("objectName"));
                order.setCurrency((String) output.getDataValue("currency"));
                order.setPayTime(pinganTimeToDate((String) output.getDataValue("date")));
                order.setOrderTime(pinganTimeToDate((String) output.getDataValue("paydate")));

                houseOrderService.payment(Integer.parseInt(order.getCustomerId()), Integer.parseInt(order.getRemark()));
                pinanOrderService.create(order);

                //资金进入会员子账户 和 冻结资金到担保账户
                wallet.frozennMoney("3",fromUserId,toUserId,amount,orderId);

                return Result.success().msg("").data(new HashMap<>());
            } else {

                return Result.error().msg(errorMsg).data(new HashMap<>());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }
    }

    @RequestMapping(value = "commissionnotify")
    public void submitNotify(@RequestParam String orig, @RequestParam String sign) {
        try {


            logger.info("---orig---" + orig);
            logger.info("---sign---" + sign);

            PayclientInterfaceUtil util = new PayclientInterfaceUtil();
            KeyedCollection output = new KeyedCollection("output");

            String encoding = "GBK";


            orig = PayclientInterfaceUtil.Base64Decode(orig, encoding);
            sign = PayclientInterfaceUtil.Base64Decode(sign, encoding);


            boolean result = util.verifyData(sign, orig);
            logger.info("---通知验签结果---" + result);
            if (!result) {
                logger.info("---验签失败---" + result);
                return;
            }

            output = util.parseOrigData(orig);

//  orig信息：
//            status	char	2	01成功，02失败，00未成功
//            date	varchar	14	支付完成时间，
//            YYYYMMDDHHMMSS
//            charge	number	12,2	订单手续费金额，12整数，2小数
//            masterId	char	10	商户号
//            orderId	varchar	26	订单号
//            currency	char	3	币种，目前只支持RMB
//            amount	number	12,2	订单金额，12整数，2小数
//            objectName	varchar	200	款项描述
//            paydate	varchar	14	下单时间，
//            YYYYMMDDHHMMSS
//            validtime	number	10	订单有效期(毫秒)，0不生效
//            remark	varchar	500	备注字段
//            customerId	varchar	30	商户会员号
//            accNo	varchar	4	银行卡号后四位
//            telephone	varchar	20	银行预留手机号
//            errorCode	varchar	8	错误返回相应的错误码, 正常返回为空
//            errorMsg	varchar	100	错误码对应的错误说明，正常返回为空

            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

//            if((errorCode == null || errorCode.replaceAll(" ","").equals(""))&& (errorMsg == null || errorCode.replaceAll(" ","").equals(""))){
//                // 成功
//                PinanOrder order = new PinanOrder();
//                order.setMasterId((String) output.getDataValue("masterId"));
//                order.setOrderId((String) output.getDataValue("orderId"));
//                order.setAmount((BigDecimal) output.getDataValue("amount"));
//                order.setCharge((BigDecimal) output.getDataValue("charge"));
//                order.setValidtime((BigDecimal) output.getDataValue("validtime"));
//                order.setCustomerId((String) output.getDataValue("customerId"));
//                order.setAccNo((String) output.getDataValue("accNo"));
//                order.setMobile((String) output.getDataValue("telephone"));
//                order.setRemark((String) output.getDataValue("remark"));
//                order.setObjectName((String) output.getDataValue("objectName"));
//                order.setCurrency((String) output.getDataValue("currency"));
//                order.setPayTime(pinganTimeToDate((String) output.getDataValue("date")));
//                order.setOrderTime(pinganTimeToDate((String) output.getDataValue("paydate")));
//
//                OrderApi api = new OrderApi();
//                api.payment(Integer.parseInt(order.getCustomerId()),Integer.parseInt(order.getRemark()));
//
//                pinanOrderService.create(order);
//
//            } else {
//                logger.info("失败原因====================" + errorMsg);
//            }

        } catch (Exception e) {
            e.printStackTrace();
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
            return Result.error().msg(Error_code.ERROR_CODE_0001).data(new HashMap<>());
        }

    }

}

