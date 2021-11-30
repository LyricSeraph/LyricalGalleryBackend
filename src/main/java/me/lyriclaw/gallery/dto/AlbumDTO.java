package me.lyriclaw.gallery.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@ApiModel("")
public class AlbumDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private Instant createdAt;

    @JsonIgnore
    private Instant updatedAt;

}
