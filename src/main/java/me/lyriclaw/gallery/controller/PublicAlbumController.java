package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.dto.AlbumDTO;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.AlbumService;
import me.lyriclaw.gallery.vo.AlbumQueryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "Public Album APIs")
@Validated
@RestController
@RequestMapping("/api/public/album")
@Slf4j
public class PublicAlbumController {

    private final AlbumService albumService;

    public PublicAlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    @ApiOperation("Retrieve by ID ")
    public ApiResp<AlbumDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        return ApiResp.success(albumService.getById(id));
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    @ApiOperation("Retrieve by query ")
    public ApiResp<Page<AlbumDTO>> query(@Valid AlbumQueryVO vO,
                                         @PageableDefault(page = 0, size = 20, sort = "album_id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResp.success(albumService.findByNameLike(vO.getName(), pageable));
    }
}
