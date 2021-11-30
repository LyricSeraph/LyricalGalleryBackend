package me.lyriclaw.gallery.throwable;


import me.lyriclaw.gallery.constants.ApiResponseStatus;
import org.springframework.http.HttpStatus;

public class ResponseException extends RuntimeException {

    private ApiResponseStatus status;
    private int httpStatus;

    public ResponseException(ApiResponseStatus type) {
        this(type, HttpStatus.INTERNAL_SERVER_ERROR.value(), type.getMessage());
    }

    public ResponseException(ApiResponseStatus type, String message) {
        this(type, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    public ResponseException(ApiResponseStatus type, int httpStatus) {
        this(type, httpStatus, type.getMessage());
    }

    public ResponseException(ApiResponseStatus type, int httpStatus, String message) {
        super(message);
        this.status = type;
        this.httpStatus = httpStatus;
    }

    public ApiResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ApiResponseStatus status) {
        this.status = status;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

}
