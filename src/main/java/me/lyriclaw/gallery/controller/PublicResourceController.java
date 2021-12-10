package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.ResourceService;
import me.lyriclaw.gallery.vo.ResourceQueryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "Public Resource APIs")
@Validated
@RestController
@RequestMapping("/public/api/resource")
@Slf4j
public class PublicResourceController {

    private final ResourceService resourceService;

    public PublicResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    @ApiOperation("Retrieve by ID ")
    public ApiResp<ResourceDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        return ApiResp.success(resourceService.getById(id));
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    @ApiOperation("Retrieve by query ")
    public ApiResp<Page<ResourceDTO>> query(@Valid ResourceQueryVO vO,
                                            @RequestParam(value = "tagId", required = false) Long tagId,
                                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ApiResp.success(resourceService.queryByAlbumAndTagAndName(vO.getAlbumId(), tagId, vO.getName(), pageable));
    }
}
