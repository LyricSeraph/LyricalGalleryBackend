package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@ApiModel("Update ")
@EqualsAndHashCode(callSuper = false)
public class ResourceUpdateVO extends ResourceVO implements Serializable {
    private static final long serialVersionUID = 1L;

}
