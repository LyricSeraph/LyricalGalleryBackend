package me.lyriclaw.gallery.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;


@Data
@ApiModel("Save ")
public class AlbumVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long albumId;

    @NotEmpty(message = "name can not be empty")
    private String name;

    private Long coverId;

    private Instant createdAt;

    private Instant updatedAt;

}
