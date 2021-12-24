package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("Retrieve tag by query ")
public class TagQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private Long albumId;

    private boolean ignoreAlbum = false;

}
