package me.lyriclaw.gallery.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("Save ")
public class ResourceTagVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long rtId;

    @NotNull(message = "tagId can not null")
    private Long tagId;

    @NotNull(message = "resourceId can not null")
    private Long resourceId;


}
