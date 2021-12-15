package me.lyriclaw.gallery.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;


@Data
@ApiModel("Save ")
public class ResourceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long resourceId;

    private String uuid;

    private String extension;

    @NotEmpty(message = "name can not be empty")
    private String name;

    private String sourceUrl;

    private Float ratio;

    private String sThumb;

    private String mThumb;

    private String lThumb;

    private Long albumId;

    private Integer failedTries;

    /**
     * idle: 0, downloading: 1, finished: 2
     */
    @NotNull(message = "status can not null")
    @ApiModelProperty("idle: 0, downloading: 1, finished: 2, failed: 3")
    private Integer status;

    private Instant createdAt;

    private Instant updatedAt;

    public String getStorageFilename() {
        return getUuid() + getExtension();
    }
}
