package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.lyriclaw.gallery.dto.TagDTO;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.TagService;
import me.lyriclaw.gallery.vo.TagUpdateVO;
import me.lyriclaw.gallery.vo.TagVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "Private Tag APIs")
@Validated
@RestController
@RequestMapping("/private/api/tag")
public class PrivateTagController {

    private final TagService tagService;

    public PrivateTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    @ApiOperation("Save ")
    public ApiResp<TagDTO> save(@Valid @RequestBody TagVO vO) {
        Long id = tagService.save(vO);
        return ApiResp.success(tagService.getById(id));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update ")
    public ApiResp<TagDTO> update(@Valid @NotNull @PathVariable("id") Long id, @Valid @RequestBody TagUpdateVO vO) {
        tagService.update(id, vO);
        return ApiResp.success(tagService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public ApiResp<Object> delete(@Valid @NotNull @PathVariable("id") Long id) {
        tagService.delete(id);
        return ApiResp.success();
    }

}
