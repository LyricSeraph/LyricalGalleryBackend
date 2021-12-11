package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Retrieve by query ")
public class ResourceTagQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long tagId;

    private Long resourceId;

}
