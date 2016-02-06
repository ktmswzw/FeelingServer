package com.xecoder.common.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

/**
 * Author: zhangxin
 * Date:   15-9-17
 */
@EnableWebMvc
@ControllerAdvice
public class FeelingExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(FeelingCommonException.class)
    @ResponseBody
    public ResponseEntity<?> FeelingExceptionHandler(FeelingCommonException ce) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTitle(messageSource.getMessage("error.info.system", null, Locale.getDefault()));
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setDetail(ce.getMessage());
        errorDetail.setTimestamp(System.currentTimeMillis());
        errorDetail.setDeveloperMessage("com.xecoder");

        return new ResponseEntity<>(errorDetail, null, HttpStatus.OK);
    }


    @Override
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(System.currentTimeMillis());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle(messageSource.getMessage("error.validation.failed", null, Locale.getDefault()));
        errorDetail.setDetail(messageSource.getMessage("error.input.validation.failed", null, Locale.getDefault()));
        errorDetail.setDeveloperMessage(manve.getClass().getName());

        List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
        for (FieldError fe : fieldErrors) {
            List<ValidationError> validationErrorList = errorDetail.getErrors().get(fe.getField());
            if (validationErrorList == null) {
                validationErrorList = new ArrayList<>();
                errorDetail.getErrors().put(fe.getField(), validationErrorList);
            }
            ValidationError validationError = new ValidationError();
            validationError.setCode(fe.getCode());
            validationError.setMessage(messageSource.getMessage(fe, null));
            validationErrorList.add(validationError);
        }

        return handleExceptionInternal(manve, errorDetail, headers, HttpStatus.OK, request);
    }

    @Override
    @ResponseBody
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers,
                                                                       HttpStatus status, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimestamp(System.currentTimeMillis());
        errorDetail.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDetail.setTitle(messageSource.getMessage("error.validation.failed", null, Locale.getDefault()));
        errorDetail.setDetail(messageSource.getMessage("error.input.validation.failed", null, Locale.getDefault()));
        errorDetail.setDeveloperMessage(ex.getClass().getName());

        List<ValidationError> validationErrorList = errorDetail.getErrors().get(ex.getParameterName());
        if (validationErrorList == null) {
            validationErrorList = new ArrayList<>();
            errorDetail.getErrors().put(ex.getParameterName(), validationErrorList);
        }
        ValidationError validationError = new ValidationError();
        validationError.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
        validationError.setMessage(messageSource.getMessage("error.parameter.validation.failed", new String[]{ex.getParameterName()+":["+ex.getParameterType()+"]"}, Locale.getDefault()));
        validationErrorList.add(validationError);

        return this.handleExceptionInternal(ex, errorDetail, headers, HttpStatus.OK, request);
    }


    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<Object> handleControllerException(Throwable ex) {
        ReturnMessage returnMessage = ((HttpServiceException) ex).getReturnMessage();
        return new ResponseEntity<>(returnMessage, HttpStatus.OK);
    }

    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> responseBody = new HashMap<>();
        ReturnMessage returnMessage = new ReturnMessage("ERROR PATH");
        return new ResponseEntity<>(returnMessage,HttpStatus.OK);
    }
}
