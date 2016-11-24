package com.ryel.springBootJpa.controller;

import com.ryel.springBootJpa.utils.CookiesUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by burgl on 2016/8/20.
 */
@Controller
public class IndexController extends BaseController{


//    @Autowired
//    private LoginService loginService;


    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response,
                        String error,
                        ModelMap model) {
        if (StringUtils.isNotBlank(error)) {
            model.put("error", error);
        }
        // 先读取cookies，如果存在，则将用户名回写到输入框
        Map<String, Object> params = CookiesUtils.ReadCookieMap(request);
        if (params != null && params.size() != 0) {
            String name = (String) params.get("name");
            model.put("name",name);
        }

        return "登录";
    }


    @RequestMapping(value = "/login/check")
    public String checkLogin(String username,
                             String password,
                             HttpServletRequest request, HttpServletResponse response,
                             String remark,
                             ModelMap model) {

//        Boolean success = loginService.login(request, username, password, remark);
        Boolean success =true;
        if (success) {
            // 登录成功后，将用户名放入cookies
            int loginMaxAge = 30 * 24 * 60 * 60; // 定义cookies的生命周期，这里是一个月。单位为秒
            CookiesUtils.addCookie(response, "name", username, loginMaxAge);

            return "redirect:/dashboard";
        }
        model.put("error", "用户名或密码错误!");
        return "redirect:/login";
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         ModelMap model) {
        loginService.logOut(request);
        return "登录";
    }

    @RequestMapping(value = "/")
    public String index(){

        return "redirect:/dashboard";
    }


    @RequestMapping(value = "/dashboard")
    public String dashboard(HttpServletRequest request,
                            HttpServletResponse response,
                            ModelMap model) {
        return "控制面板";
    }



}
