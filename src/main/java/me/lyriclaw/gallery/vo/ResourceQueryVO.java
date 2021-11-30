package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@ApiModel("Retrieve by query ")
public class ResourceQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String uuid;

    private String extension;

    private String name;

    private String url;

    private String sourceUrl;

    private Long albumId;

    private Integer failedTries;
    /**
     * idle: 0, downloading: 1, finished: 2
     */
    @ApiModelProperty("idle: 0, downloading: 1, finished: 2")
    private Integer status;

    private Instant createdAt;

    private Instant updatedAt;

}
