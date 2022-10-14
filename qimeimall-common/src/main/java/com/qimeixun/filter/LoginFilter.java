package com.qimeixun.filter;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenshouyang
 * @date 2020/4/2615:55
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*",filterName = "LoginFilter")
public class LoginFilter implements Filter {

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {
        HttpServletResponse response = crossFilter(arg1);
        HttpServletRequest request = (HttpServletRequest) arg0;
        arg2.doFilter(request, response);
    }


    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    private HttpServletResponse crossFilter(ServletResponse arg1){
        HttpServletResponse response = (HttpServletResponse) arg1;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        //response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,Authorization,Accept,User-Agent,Referer,Token,Area");
        //response.setHeader("Access-Control-Request-Headers", "Origin, X-Requested-With, content-Type, Accept, Authorization");
        response.setHeader("Access-Control-Allow-Credentials","true");
        return response;
    }



}
