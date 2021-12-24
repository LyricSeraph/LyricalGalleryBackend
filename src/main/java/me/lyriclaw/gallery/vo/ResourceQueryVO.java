package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Retrieve resource by query")
public class ResourceQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private Long albumId;

     /**
     * idle: 0, downloading: 1, finished: 2, failed: 3
     */
    @ApiModelProperty("idle: 0, downloading: 1, finished: 2, failed: 3")
    private Integer status;

    private boolean ignoreAlbum = false;

}
