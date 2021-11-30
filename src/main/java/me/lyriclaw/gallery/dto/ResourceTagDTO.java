package me.lyriclaw.gallery.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@ApiModel("")
public class ResourceTagDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Long id;

    private Long tagId;

    private Long resourceId;

}
