package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.lyriclaw.gallery.dto.ResourceTagDTO;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.service.ResourceTagService;
import me.lyriclaw.gallery.vo.ResourceTagQueryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "Public ResourceTag APIs")
@Validated
@RestController
@RequestMapping("/public/api/resourceTag")
public class PublicResourceTagController {

    private final ResourceTagService resourceTagService;

    public PublicResourceTagController(ResourceTagService resourceTagService) {
        this.resourceTagService = resourceTagService;
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    @ApiOperation("Retrieve by ID ")
    public ApiResp<ResourceTagDTO> getById(@Valid @NotNull @PathVariable("id") Long id) {
        return ApiResp.success(resourceTagService.getById(id));
    }

    @RequestMapping(value = "", method = {RequestMethod.GET})
    @ApiOperation("Retrieve by query ")
    public ApiResp<Page<ResourceTagDTO>> query(@Valid ResourceTagQueryVO vO,
                                               @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResp.success(resourceTagService.query(vO, pageable));
    }


}
