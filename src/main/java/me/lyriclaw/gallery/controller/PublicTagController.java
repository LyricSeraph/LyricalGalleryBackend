package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.lyriclaw.gallery.dto.TagDTO;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.TagService;
import me.lyriclaw.gallery.vo.TagQueryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "Public Tag APIs")
@Validated
@RestController
@RequestMapping("/public/api/tag")
public class PublicTagController {

    private final TagService tagService;

    public PublicTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve by ID ")
    public ApiResp<TagDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        return ApiResp.success(tagService.getById(id));
    }

    @GetMapping
    @ApiOperation("Retrieve by query ")
    public ApiResp<List<TagDTO>> query(@Valid TagQueryVO vO) {
        return ApiResp.success(tagService.query(vO));
    }
}
