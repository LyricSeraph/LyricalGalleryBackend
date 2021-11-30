package me.lyriclaw.gallery.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.lyriclaw.gallery.constants.PreviewSize;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@ApiModel("")
public class ResourceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonIgnore
    private String uuid;

    @JsonIgnore
    private String extension;

    private String name;

    @JsonIgnore
    private String sourceUrl;

    private Long albumId;

    @JsonIgnore
    private Integer failedTries;

    /**
     * idle: 0, downloading: 1, finished: 2
     */
    @ApiModelProperty("idle: 0, downloading: 1, finished: 2")
    private Integer status;

    private Instant createdAt;

    @JsonIgnore
    private Instant updatedAt;

    private List<ResourceTagDTO> tags;

    @JsonIgnore
    public String getStorageFilename() {
        return getUuid() + getExtension();
    }

    public String getUrl() {
        return "/resources/" + getStorageFilename();
    }

    public String getSmallThumbnailUrl() {
        return "/thumbnails/" + getStorageFilename() + "_" + PreviewSize.small + ".png";
    }

    public String getMediumThumbnailUrl() {
        return "/thumbnails/" + getStorageFilename() + "_" + PreviewSize.medium + ".png";
    }

    public String getLargeThumbnailUrl() {
        return "/thumbnails/" + getStorageFilename() + "_" + PreviewSize.large + ".png";
    }


}
