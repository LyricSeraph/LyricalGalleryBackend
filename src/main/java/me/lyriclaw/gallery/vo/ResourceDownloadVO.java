package me.lyriclaw.gallery.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ResourceDownloadVO {

    @NotEmpty
    private String url;

    @NotNull
    private Long albumId;

    private String name;

}
