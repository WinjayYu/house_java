package com.ryel.springBootJpa.fillter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by burgl on 2016/8/21.
 */
public class LoginFilter implements Filter {

    private static final Logger logger = Logger.getLogger(LoginFilter.class);

    private static String[] SKIP_URLS = new String[]{};


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String urls = filterConfig.getInitParameter("skipUrls");
        if (StringUtils.isNotBlank(urls)) {
            String temp[] = urls.split(",");
            List<String> list = new ArrayList<String>();

            for (String skipUrl : temp) {
                if (StringUtils.isNotBlank(skipUrl)) {
                    skipUrl = "^" + skipUrl.replaceAll("\\*", ".*") + "$";
                    list.add(skipUrl);
                }
            }
            SKIP_URLS = list.toArray(SKIP_URLS);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI().toString();
        String contextPath = httpRequest.getContextPath();
        url = url.substring(contextPath.length());

        logger.info(url);

        if (SKIP_URLS != null) {
            for (String skipUrl : SKIP_URLS) {
                Pattern pattern = Pattern.compile(skipUrl, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }
//        Member member = (Member)httpRequest.getSession().getAttribute(Constant.SESSION_MEMBER);
//        if(member!=null){
            chain.doFilter(request, response);
            return;
//        }else{
//            httpResponse.sendRedirect(contextPath+"/login");
//        }



    }

    @Override
    public void destroy() {

    }
}
