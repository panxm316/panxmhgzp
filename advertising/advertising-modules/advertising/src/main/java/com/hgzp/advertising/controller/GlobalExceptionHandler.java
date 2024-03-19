package com.hgzp.advertising.controller;

import com.hgzp.core.exception.*;
import com.hgzp.core.page.Json;
import com.hgzp.utils.exception.FileUploadUnFinshException;
import com.hgzp.utils.exception.UfileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import static com.hgzp.core.emnus.ResultDefines.*;


@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);




    @ExceptionHandler(FileUploadUnFinshException.class)
    @ResponseBody
    public Json fileUploadUnFinshExceptionHandler(FileUploadUnFinshException e) {
        return Json.fail(FILE_UPLOAD_UNFINISHED);
    }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseBody
    public Json optimisticLockingFailureExceptionHandler(OptimisticLockingFailureException e) {
        return Json.fail(DATA_OUT_OF_DATE);
    }

    @ExceptionHandler(InnerAuthException.class)
    @ResponseBody
    public Json innerAuthExceptionHandler(InnerAuthException e) {
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(NOT_AUTHORIZATION.getCode(), e.getMessage());
        }
        return Json.fail(NOT_AUTHORIZATION);
    }

    @ExceptionHandler(CaptchaException.class)
    @ResponseBody
    public Json captchaExceptionHandler(CaptchaException e) {
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(KAPTCHA_EXCEPTION.getCode(), e.getMessage());
        }
        return Json.fail(KAPTCHA_EXCEPTION);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public Json illegalStateExceptionHandler(IllegalStateException e) {
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(IllegalState.getCode(), e.getMessage());
        }
        return Json.fail(IllegalState);
    }

    @ExceptionHandler(DataNotSupportException.class)
    @ResponseBody
    public Json dataNotSupportExceptionHandler(DataNotSupportException e) {
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(DATA_NOT_SUPPORT.getCode(), e.getMessage());
        }
        return Json.fail(DATA_NOT_SUPPORT);
    }

    @ExceptionHandler(DataExistException.class)
    @ResponseBody
    public Json dataExistExceptionHandler(DataExistException e) {
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(DATA_NOT_SUPPORT.getCode(), e.getMessage());
        }
        return Json.fail(DATA_NOT_SUPPORT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public Json illegalArgumentExceptionHandler(IllegalArgumentException e) {
        logger.error("参数错误", e);
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(IllegalArgument.getCode(), e.getMessage());
        }
        return Json.fail(IllegalArgument);
    }

    @ExceptionHandler(DataNotExistException.class)
    @ResponseBody
    public Json dataNotExistExceptionHandler(DataNotExistException e) {
        logger.error("数据异常", e);
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(DATA_NOTEXIT.getCode(), e.getMessage());
        }
        return Json.fail(DATA_NOTEXIT);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public Json authorizationExceptionHandler(AuthorizationException e) {
        logger.error("身份验证异常", e);
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(USER_NOT_LOGGED_IN.getCode(), e.getMessage());
        }
        return Json.fail(USER_NOT_LOGGED_IN);
    }

    @ExceptionHandler(UfileException.class)
    @ResponseBody
    public Json ufileExceptionHandler(UfileException e) {
        logger.error("统一文件异常", e);
        if(StringUtils.hasText(e.getMessage())){
            return Json.fail(UFILE_EXCEPTION.getCode(), e.getMessage());
        }
        return Json.fail(UFILE_EXCEPTION);
    }


    /**
     * 定义@Valid注解全局异常处理机制
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Json validationBodyException(BindException result){
        String errmsg = "";
        if (result.hasErrors()) {
            List<FieldError> errorlist = result.getFieldErrors();
            for (FieldError fieldError : errorlist) {
                errmsg += fieldError.getField() + ":" + fieldError.getDefaultMessage() + " ";
            }
        }
        return Json.fail(IllegalArgument.getCode(), errmsg);
    }

    //处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Json constraintViolationExceptionHandler(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining());
        return Json.fail(IllegalArgument.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Json exception(MethodArgumentNotValidException e) {
        String errmsg = "";
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> errorlist = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorlist) {
            errmsg += fieldError.getField() + ":" + fieldError.getDefaultMessage() + " ";
        }
        return Json.fail(IllegalArgument.getCode(), errmsg);
    }



    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Json runtimeExceptionHandler(RuntimeException e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        long code = System.currentTimeMillis();
        logger.error(code+"" , e);
        return Json.fail(SYS_ERROR.getCode(), SYS_ERROR.getMessage()+code);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Json exceptionHandler(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        long code = System.currentTimeMillis();
        logger.error(code + "" , e);
        return Json.fail(SYS_ERROR.getCode(), SYS_ERROR.getMessage()+code);
    }

    @ExceptionHandler(CusIllegalArgumentException.class)
    @ResponseBody
    public Json cusIllegalArgumentExceptionHandler(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        logger.error(e.getMessage());
        return Json.fail(SYS_ERROR.getCode(), e.getMessage());
    }
}
