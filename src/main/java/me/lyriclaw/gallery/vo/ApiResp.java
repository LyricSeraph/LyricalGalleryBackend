package me.lyriclaw.gallery.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.constants.ApiResponseStatus;

import static me.lyriclaw.gallery.constants.ApiResponseStatus.STATUS_OK;

@Data
@AllArgsConstructor
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResp<T> {

    private int status;
    private String message;
    private T data;

    /**
     * Page results
     */
    private Long total;
    private Integer page;
    private Integer pageSize;

    public static <T> ApiResp<T> create(ApiResponseStatus status, String message, T data) {
        return new ApiResp<>(status.getCode(), message, data, null, null, null);
    }

    public static <T> ApiResp<T> success() {
        return create(STATUS_OK, STATUS_OK.getMessage(), null);
    }

    public static <T> ApiResp<T> success(T data) {
        return create(STATUS_OK, STATUS_OK.getMessage(), data);
    }

    public static <T> ApiResp<T> error(ApiResponseStatus code) {
        return create(code, code.getMessage(), null);
    }

    public static <T> ApiResp<T> error(ApiResponseStatus code, String message) {
        return create(code, message, null);
    }


}
