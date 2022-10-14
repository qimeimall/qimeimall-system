package com.qimeixun.exceptions;

import com.qimeixun.util.Tool;
import com.qimeixun.vo.ResponseResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.util.Set;

/**
 * 异常处理类
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class CommonExceptionAdvice extends CommonHandel {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseResultVO handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("缺少请求参数", e);
        return ResponseResultVO.failResult("缺少请求参数：" + e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResultVO handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("参数解析失败", e);
        return ResponseResultVO.failResult("缺少请求参数：" + e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResultVO handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数验证失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        return ResponseResultVO.failResult(error.getDefaultMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public ResponseResultVO handleBindException(BindException e) {
        log.error("参数绑定失败", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return ResponseResultVO.failResult("参数数据类型绑定失败：" + message);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResultVO handleServiceException(ConstraintViolationException e) {
        log.error("参数验证失败", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return  ResponseResultVO.failResult(message);

    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ValidationException.class)
    public ResponseResultVO handleValidationException(ValidationException e) {
        log.error("参数验证失败", e);
        return  ResponseResultVO.failResult(e.getMessage());
    }

    /**
     * 404 - Not Found
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseResultVO noHandlerFoundException(NoHandlerFoundException e) {
        log.error("Not Found", e);
        return ResponseResultVO.failResult("请求URL地址不存在：");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResultVO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法", e);
        return ResponseResultVO.failResult("请求方法错误：" + e.getMethod());
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseResultVO handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型", e);
        return ResponseResultVO.failResult("不支持当前媒体类型");
    }

    /**
     * 业务层需要自己声明异常的情况
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public ResponseResultVO handleServiceException(ServiceException e) {
        log.error("业务逻辑异常", e);
        return ResponseResultVO.failResult(e.getMessage());
    }

    /**
     * 操作数据或库出现异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataDoException.class)
    public ResponseResultVO handleException(DataDoException e) {
        log.error("操作数据库出现异常:", e);
        return ResponseResultVO.failNoLoginResult("操作数据库出现异常：字段重复、有外键关联等");
    }

    /**
     * 未登录
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoLoginException.class)
    public ResponseResultVO NoLoginException(NoLoginException e) {
        log.error("未登录，请先登录:", e);
        return ResponseResultVO.failNoLoginResult("请先登录");
    }


    /**
     * 无操作权限
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoPermissionException.class)
    public ResponseResultVO NoPermissionException(NoPermissionException e) {
        log.error("无操作权限:", e);
        return ResponseResultVO.failNoLoginResult("请先登录");
    }

    /**
     * 文件不存在
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseResultVO FileNotFoundException(FileNotFoundException e) {
        log.error("文件不存在:", e);
        return ResponseResultVO.failNoLoginResult("请先登录");
    }

    /**
     * 文件上传大小超出限制
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResultVO MaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("上传文件大小超出限制:", e);
        String msg = "上传单个文件最大大小为：" + maxFileSize + "，单次上传总文件最大大小为：" + maxRequestSize;
        return ResponseResultVO.failNoLoginResult("请先登录");
    }

    /**
     * 获取其它异常。包括500
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseResultVO defaultErrorHandler(Exception e) {
        log.error("Exception", e);
        String msg = Tool.isBlank(e.getMessage()) ? "系统异常，请联系管理员解决" : e.getMessage();
        return ResponseResultVO.failResult("系统异常，请联系管理员解决");
    }

}
