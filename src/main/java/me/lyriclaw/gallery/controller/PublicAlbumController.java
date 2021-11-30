package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.lyriclaw.gallery.dto.AlbumDTO;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.AlbumService;
import me.lyriclaw.gallery.vo.AlbumQueryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "Public Album APIs")
@Validated
@RestController
@RequestMapping("/public/api/album")
public class PublicAlbumController {

    private final AlbumService albumService;

    public PublicAlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public ApiResp<AlbumDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        return ApiResp.success(albumService.getById(id));
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public ApiResp<Page<AlbumDTO>> query(@Valid AlbumQueryVO vO,
                                         @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResp.success(albumService.query(vO, pageable));
    }
}
