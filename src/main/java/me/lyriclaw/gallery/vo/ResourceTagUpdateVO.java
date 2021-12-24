package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("Update resource-tag")
@EqualsAndHashCode(callSuper = false)
public class ResourceTagUpdateVO extends ResourceTagVO implements Serializable {
    private static final long serialVersionUID = 1L;

}
