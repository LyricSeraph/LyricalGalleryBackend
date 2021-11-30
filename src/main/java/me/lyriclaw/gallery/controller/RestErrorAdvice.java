package me.lyriclaw.gallery.controller;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.throwable.ResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice(basePackages = "me.lyriclaw.gallery")
@Slf4j
public class RestErrorAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    protected ResponseEntity<Object> handleResponseException(ResponseException ex, WebRequest request) {
        ApiResp<Object> apiResp = ApiResp.error(ex.getStatus(), ex.getMessage());
        return handleExceptionInternal(ex, apiResp, new HttpHeaders(), HttpStatus.valueOf(ex.getHttpStatus()), request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.debug("handleAll: " +
                "ex = [" + ex + "], request = [" + request + "]", ex);
        ApiResp<Object> apiError = ApiResp.error(ApiResponseStatus.STATUS_INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
