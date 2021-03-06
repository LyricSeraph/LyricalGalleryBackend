package me.lyriclaw.gallery.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@ApiModel("")
public class AlbumDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long albumId;

    private String name;

    private Long parentId;

    private Long coverId;

    private Instant createdAt;

    @JsonIgnore
    private Instant updatedAt;

    private List<ResourceDTO> sampleResources;

    private long albumSize;

    private long subAlbumCount;

    private AlbumDTO parent;
}
