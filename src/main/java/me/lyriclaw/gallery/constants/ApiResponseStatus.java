package me.lyriclaw.gallery.constants;

import java.util.HashMap;
import java.util.Map;

public enum ApiResponseStatus {

    /**
     * 通用状态
     */
    STATUS_INTERNAL_SERVER_ERROR(-1, "Internal server error"),
    STATUS_OK(0x0, "Success"),
    STATUS_NOT_AUTHORIZED(0x1, "Operation not authorized"),
    STATUS_LOGIN_REQUIRED(0x2, "Login required"),
    STATUS_NOT_FOUND(0x3, "Resource not found"),

    STATUS_PARAMETER_INVALID(0x10000, "Parameter invalid"),
    STATUS_STORAGE_ERROR(0x10001, "Storage error"),
    STATUS_ILLEGAL_IMAGE_FORMAT(0x10002, "Illegal image format"),
    ;

    private static Map<Integer, String> responseMessageMap;

    static {
        responseMessageMap = new HashMap<>();
        for (ApiResponseStatus statusType : ApiResponseStatus.values()) {
            responseMessageMap.put(statusType.code, statusType.message);
        }
    }

    public static String getMessage(int code) {
        return responseMessageMap.get(code);
    }

    private int code;
    private String message;

    ApiResponseStatus(int code) {
        this.code = code;
        this.message = "server error";
    }

    ApiResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
