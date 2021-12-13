package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("Update ")
public class ResourceUpdateVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private Long albumId;

}
