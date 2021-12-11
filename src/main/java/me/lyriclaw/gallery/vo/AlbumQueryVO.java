package me.lyriclaw.gallery.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@ApiModel("Retrieve by query ")
public class AlbumQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long albumId;

    private String name;

    private Long coverId;

    private Instant createdAt;

    private Instant updatedAt;

}
