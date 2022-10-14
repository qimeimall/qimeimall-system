package com.qimeixun.aspect;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.qimeixun.mapper.SysLogMapper;
import com.qimeixun.po.LogDTO;
import com.qimeixun.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.net.util.IPAddressUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

// 定义一个切面
@Aspect
@Configuration
@Slf4j
public class LogRecordAspect {


    @Resource
    LogRecord logRecord;

    @Resource
    TokenUtil tokenUtil;


    // 定义切入点
    @Pointcut("execution(* com.qimeixun.*.*.controller..*.*(..))")
    public void controllerPointcut() {
    }

    // 定义事件触发节点，引用切入点
    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String url = request.getRequestURI();
        String method = request.getMethod();
        Map<String, String> paramMap = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            paramMap.put(name, value);
        }
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method methodTemp = signature.getMethod();
        ApiOperation apiOperation = methodTemp.getAnnotation(ApiOperation.class);
        String methodName = "";
        if(apiOperation != null){
            methodName = apiOperation.value();
        }

        log.info("请求开始===地址:{}", url);
        log.info("请求开始===类型:{}", method);
        log.info("请求开始===参数:{}", JSON.toJSONString(paramMap));
        // result的值就是被拦截方法的返回值
        String resultStr = "";
        Object result = null;
        String type = "info";
        long startTime = new Date().getTime();
        try {
            result = pjp.proceed();
//            resultStr = JSON.toJSONString(result);
        } catch (Throwable throwable) {
            log.error("error=",throwable.getMessage());
            type = "error";
            resultStr = JSON.toJSONString(throwable);
            throw throwable;
        }finally {
            long endTime = new Date().getTime();
            log.info("请求结束===返回值:{}", JSON.toJSONString(result));
            LogDTO logDTO = new LogDTO();
            logDTO.setLogId(IdUtil.getSnowflake(1, 1).nextIdStr());
            logDTO.setMethod(method);
            logDTO.setRequestUri(url);
            logDTO.setTitle(methodName);
            logDTO.setType(type);
            logDTO.setOperateDate(new Date());
            logDTO.setResultParams(resultStr);
            logDTO.setTimeout(endTime - startTime);
            logDTO.setUserId(tokenUtil.getUnCheckUserIdByToken());
            logDTO.setRemoteAddr(tokenUtil.getV4IP(request));
            logRecord.inertLogRecord(logDTO);
        }
        return result;
    }
}