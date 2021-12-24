package me.lyriclaw.gallery.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("Save resource")
public class ResourceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String extension;

    @NotEmpty(message = "name can not be empty")
    private String name;

    private String sourceUrl;

    private Float ratio;

    private Long albumId;

    /**
     * idle: 0, downloading: 1, finished: 2
     */
    @NotNull(message = "status can not null")
    @ApiModelProperty("idle: 0, downloading: 1, finished: 2, failed: 3")
    private Integer status;

}
