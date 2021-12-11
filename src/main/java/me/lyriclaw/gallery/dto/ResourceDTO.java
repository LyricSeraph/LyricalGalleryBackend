package me.lyriclaw.gallery.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.lyriclaw.gallery.constants.PreviewSize;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@ApiModel("")
public class ResourceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long resourceId;

    @JsonIgnore
    private String uuid;

    @JsonIgnore
    private String extension;

    private String name;

    @JsonIgnore
    private String sourceUrl;

    private Float ratio;

    @JsonIgnore
    private String sThumb;

    @JsonIgnore
    private String mThumb;

    @JsonIgnore
    private String lThumb;

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
        return "/thumbnails/" + getSThumb();
    }

    public String getMediumThumbnailUrl() {
        return "/thumbnails/" + getMThumb();
    }

    public String getLargeThumbnailUrl() {
        return "/thumbnails/" + getLThumb();
    }


}
