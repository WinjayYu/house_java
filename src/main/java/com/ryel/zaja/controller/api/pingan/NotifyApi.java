package com.ryel.zaja.controller.api.pingan;

import com.ecc.emp.data.KeyedCollection;
import com.ryel.zaja.config.Error_code;
import com.ryel.zaja.config.bean.Result;
import com.ryel.zaja.controller.api.PinganApi;
import com.ryel.zaja.utils.VerifyCodeUtil;
import com.sdb.payclient.core.PayclientInterfaceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by Nathan on 2017/2/11.
 */
@RestController
@RequestMapping(value = "/api/pingan/notify")
public class NotifyApi {

    protected final static Logger logger = LoggerFactory.getLogger(NotifyApi.class);

    @RequestMapping(value = "commissionnotify", method = RequestMethod.POST)
    public void submitNotify(HttpServletRequest request) {
        try {

            String orig = request.getParameter("orig");
            String sign = request.getParameter("sign");


            PayclientInterfaceUtil util = new PayclientInterfaceUtil();
            KeyedCollection output = new KeyedCollection("output");

            String encoding = "GBK";


            orig = PayclientInterfaceUtil.Base64Decode(orig, encoding);
            sign = PayclientInterfaceUtil.Base64Decode(sign, encoding);


            boolean result = util.verifyData(sign, orig);
            logger.info("---通知验签结果---" + result);
            if (!result) {
                logger.info("---验签失败---" + result);
            }

            output = util.parseOrigData(orig);


            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");
            String status = (String) output.getDataValue("status");

            logger.info("---支付回调---" + status);


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
