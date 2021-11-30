package me.lyriclaw.gallery.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;


@Data
@ApiModel("Save ")
public class TagVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "name can not null")
    private String name;

    private Instant createdAt;

    private Instant updatedAt;

}