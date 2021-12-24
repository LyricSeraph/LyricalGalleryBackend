package me.lyriclaw.gallery.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("Download resource")
public class ResourceDownloadVO {

    @NotEmpty(message = "url cannot empty")
    private String url;

    @NotNull(message = "album cannot empty")
    private Long albumId;

    private String name;

    private List<String> tags;

}
