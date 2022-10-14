package com.qimeixun.filter;

import com.alibaba.fastjson.JSON;
import com.qimeixun.annotation.CheckPermission;
import com.qimeixun.annotation.JwtIgnore;
import com.qimeixun.config.Audience;
import com.qimeixun.util.JwtUtil;
import com.qimeixun.vo.ResponseResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private Audience audience;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 忽略带JwtIgnore注解的请求, 不做后续token认证校验
        System.out.println(handler.getClass());
//        if(handler != null){
//            return true;
//        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        JwtIgnore jwtIgnore = handlerMethod.getMethodAnnotation(JwtIgnore.class);
        if (jwtIgnore != null) {
            return true;
        }

        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 获取请求头信息authorization信息
        final String authHeader = request.getHeader(JwtUtil.AUTH_HEADER_KEY);
        log.info("## authHeader= {}", authHeader);

        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
            log.info("### 用户未登录，请先登录 ###");
            responseMessage(response);
            return false;
        }

        // 获取token
        final String token = authHeader.substring(7);

        if (audience == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            audience = (Audience) factory.getBean("audience");
        }

        // 验证token是否有效--无效已做异常抛出，由全局异常处理后返回对应信息
        JwtUtil.parseJWT(token, audience.getBase64Secret());

        CheckPermission checkPermission = handlerMethod.getMethodAnnotation(CheckPermission.class);
        if (checkPermission != null) {
            //校验权限
            List<String> permission = JwtUtil.getPermission(token, audience.getBase64Secret());
            if(!checkPermission(checkPermission.permission(), permission)){
                responseNoPermissionMessage(response);
                return false;
            }
        }

        return true;
    }

    private boolean checkPermission(String p, List<String> permissions) {
        if(permissions == null){
            return false;
        }
        for (String s : permissions) {
            if(s.equals(p)){
                return true;
            }
        }
        return false;
    }


    //请求不通过，返回错误信息给客户端
    private void responseMessage(HttpServletResponse response) {
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().write(JSON.toJSONString(ResponseResultVO.failNoLoginResult("请登陆")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseNoPermissionMessage(HttpServletResponse response) {
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().write(JSON.toJSONString(ResponseResultVO.failResult("无权限")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
