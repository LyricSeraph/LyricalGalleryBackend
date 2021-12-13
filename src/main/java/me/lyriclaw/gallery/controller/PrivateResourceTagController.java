package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.ResourceTagService;
import me.lyriclaw.gallery.vo.ResourceTagUpdateVO;
import me.lyriclaw.gallery.vo.ResourceTagVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "Private ResourceTag APIs")
@Validated
@RestController
@RequestMapping("/private/api/resourceTag")
public class PrivateResourceTagController {

    private final ResourceTagService resourceTagService;

    public PrivateResourceTagController(ResourceTagService resourceTagService) {
        this.resourceTagService = resourceTagService;
    }

    @PostMapping
    @ApiOperation("Save ")
    public ApiResp<Long> save(@Valid @RequestBody ResourceTagVO vO) {
        return ApiResp.success(resourceTagService.save(vO));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public ApiResp<Object> delete(@Valid @NotNull @PathVariable("id") Long id) {
        resourceTagService.delete(id);
        return ApiResp.success();
    }

}
