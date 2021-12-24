package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@ApiModel("Update resource")
public class ResourceUpdateVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "name can not be empty")
    private String name;

    private Long albumId;

}
