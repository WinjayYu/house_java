package com.ryel.zaja.controller.api.pingan;

import com.ecc.emp.data.KeyedCollection;
import com.ryel.zaja.controller.api.PinganApi;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Nathan on 2017/2/11.
 */
@RestController()
@RequestMapping(value = "/api/pingan/notify", produces = "application/x-www-form-urlencoded; charset=UTF-8")
public class NotifyApi {

    protected final static Logger logger = LoggerFactory.getLogger(NotifyApi.class);

    @RequestMapping(value = "commissionnotify", method = RequestMethod.POST)
    public void submitNotify(String orig,String sign) {
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


            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
