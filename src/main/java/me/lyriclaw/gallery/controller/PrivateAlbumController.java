package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/private/api/album")
public class PrivateAlbumController {

    private final AlbumService albumService;

    public PrivateAlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping
    @ApiOperation("Save ")
    public ApiResp<Long> save(@Valid @RequestBody AlbumVO vO) {
        return ApiResp.success(albumService.save(vO));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public ApiResp<Object> delete(@Valid @NotNull @PathVariable("id") Long id) {
        albumService.delete(id);
        return ApiResp.success();
    }

}
