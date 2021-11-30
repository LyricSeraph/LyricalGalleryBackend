package me.lyriclaw.gallery.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@ApiModel("")
public class TagDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    @JsonIgnore
    private Instant createdAt;

    @JsonIgnore
    private Instant updatedAt;

}
