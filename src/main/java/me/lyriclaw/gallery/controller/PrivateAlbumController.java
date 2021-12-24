package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.lyriclaw.gallery.dto.AlbumDTO;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.AlbumService;
import me.lyriclaw.gallery.vo.AlbumUpdateVO;
import me.lyriclaw.gallery.vo.AlbumVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "Private Album APIs")
@Validated
@RestController
@RequestMapping("/api/private/album")
public class PrivateAlbumController {

    private final AlbumService albumService;

    public PrivateAlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping
    @ApiOperation("Create new album")
    public ApiResp<AlbumDTO> save(@Valid @RequestBody AlbumVO vO) {
        Long id = albumService.save(vO);
        return ApiResp.success(albumService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete album by id")
    public ApiResp<Object> delete(@Valid @NotNull @PathVariable("id") Long id) {
        albumService.delete(id);
        return ApiResp.success();
    }

}
