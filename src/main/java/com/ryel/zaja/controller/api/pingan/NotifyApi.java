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
@RequestMapping(value = "/api/pingan/notify", produces = "text/html; charset=gbk")
public class NotifyApi {

    protected final static Logger logger = LoggerFactory.getLogger(NotifyApi.class);

    @RequestMapping(value = "commissionnotify", method = RequestMethod.POST)
    public Result submitNotify(HttpServletRequest request) {
        try {

            String textEntity = VerifyCodeUtil.send("15007184046", "回调成功", "1");



            String orig = request.getParameter("orig");
            String sign = request.getParameter("sign");

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
                return Result.error().msg("验证失败").data(new HashMap<>());
            }

            output = util.parseOrigData(orig);


            String errorCode = (String) output.getDataValue("errorCode");
            String errorMsg = (String) output.getDataValue("errorMsg");

            return Result.success().data(new HashMap<>());

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error().msg("验证失败").data(new HashMap<>());

        }
    }
}
