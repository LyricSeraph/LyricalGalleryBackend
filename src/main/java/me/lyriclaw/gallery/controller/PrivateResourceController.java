package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.constants.DownloadStatus;
import me.lyriclaw.gallery.constants.PreviewSize;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.dto.ResourceTagDTO;
import me.lyriclaw.gallery.dto.TagDTO;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
import me.lyriclaw.gallery.service.ResourceService;
import me.lyriclaw.gallery.service.ResourceTagService;
import me.lyriclaw.gallery.service.StorageService;
import me.lyriclaw.gallery.service.TagService;
import me.lyriclaw.gallery.utils.FilenameUtils;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.vo.ResourceDownloadVO;
import me.lyriclaw.gallery.vo.ResourceTagVO;
import me.lyriclaw.gallery.vo.ResourceUpdateVO;
import me.lyriclaw.gallery.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Api(tags = "Private Resource APIs")
@Validated
@RestController
@RequestMapping("/api/private/resource")
@Slf4j
public class PrivateResourceController {

    private final ResourceService resourceService;
    private final StorageService storageService;
    private final TagService tagService;
    private final ResourceTagService resourceTagService;

    @Autowired
    public PrivateResourceController(ResourceService resourceService, StorageService storageService, TagService tagService, ResourceTagService resourceTagService) {
        this.resourceService = resourceService;
        this.storageService = storageService;
        this.tagService = tagService;
        this.resourceTagService = resourceTagService;
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete resource by id")
    public ApiResp<Object> delete(@Valid @NotNull @PathVariable("id") Long id) {
        ResourceDTO resourceDTO = resourceService.getById(id);
        resourceService.delete(id);
        storageService.delete(resourceDTO.getStorageFilename());
        return ApiResp.success();
    }

    @PutMapping("/{id}")
    @ApiOperation("Update resource by id")
    public ApiResp<ResourceDTO> update(@Valid @NotNull @PathVariable("id") Long id, @RequestBody ResourceUpdateVO vO) {
        resourceService.update(id, vO);
        return ApiResp.success(resourceService.getById(id));
    }

    @PostMapping("/{id}/tag/{tagId}")
    @ApiOperation("Add tag to resource")
    public ApiResp<ResourceTagDTO> addTag(@Valid @NotNull @PathVariable("id") Long id,
                                          @Valid @NotNull @PathVariable("tagId") Long tagId) {
        ResourceTagVO resourceTagVO = new ResourceTagVO();
        resourceTagVO.setResourceId(id);
        resourceTagVO.setTagId(tagId);
        Long rtId = resourceTagService.save(resourceTagVO);
        return ApiResp.success(resourceTagService.getById(rtId));
    }

    @DeleteMapping("/{id}/tag/{tagId}")
    @ApiOperation("Delete tag from resource")
    public ApiResp<Object> deleteTag(@Valid @NotNull @PathVariable("id") Long id,
                                          @Valid @NotNull @PathVariable("tagId") Long tagId) {
        resourceTagService.deleteByResourceIdTagId(id, tagId);
        return ApiResp.success();
    }

    @PostMapping("/download")
    @ApiOperation("Create download resource task")
    @Transactional
    public ApiResp<ResourceDTO> download(@Valid @RequestBody ResourceDownloadVO vO) {
        URL url;
        try {
            url = new URL(vO.getUrl());
        } catch (Exception ignored) {
            return ApiResp.error(ApiResponseStatus.STATUS_PARAMETER_INVALID);
        }

        // create record
        String filename = Paths.get(url.getPath()).getFileName().toString();
        String extension = FilenameUtils.getExtension(filename);
        if (StringUtils.hasLength(vO.getName())) {
            filename = vO.getName();
            if (!StringUtils.hasLength(extension)) {
                extension = FilenameUtils.getExtension(filename);
            }
        }
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setName(filename);
        resourceVO.setExtension(extension);
        resourceVO.setSourceUrl(vO.getUrl());
        resourceVO.setAlbumId(vO.getAlbumId());
        resourceVO.setStatus(DownloadStatus.IDLE.getStatusCode());
        resourceVO.setRatio(1F);
        Long id = resourceService.save(resourceVO);

        // add tags
        if (vO.getTags() != null) {
            for (String tagName: vO.getTags()) {
                TagDTO tagDTO = tagService.getByNameOrCreate(tagName);
                ResourceTagVO rt = new ResourceTagVO();
                rt.setResourceId(id);
                rt.setTagId(tagDTO.getTagId());
                resourceTagService.save(rt);
            }
        }

        return ApiResp.success(resourceService.getById(id));
    }

    @PostMapping("/upload")
    @ApiOperation("Upload new resource")
    public ApiResp<ResourceDTO> upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "album", required = false) Long albumId) {
        // create record
        String originFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originFilename);
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setExtension(extension);
        resourceVO.setName(originFilename);
        resourceVO.setSourceUrl(null);
        resourceVO.setAlbumId(albumId);
        resourceVO.setRatio(1F);
        resourceVO.setStatus(DownloadStatus.FINISHED.getStatusCode());
        Long id = resourceService.save(resourceVO);
        ResourceDTO result = resourceService.getById(id);

        // save file
        StorageService.StorageResult storageResult = storageService.store(file, result.getStorageFilename());
        if (!storageResult.isSuccess()) {
            return ApiResp.error(ApiResponseStatus.STATUS_STORAGE_ERROR);
        }

        // update thumbnails
        if (storageResult.getThumbnails() != null) {
            resourceService.updateResourceThumbnails(id, storageResult.getThumbnails());
        }
        return ApiResp.success(resourceService.getById(id));

    }

    @PostMapping("/restore")
    @ApiOperation("Restore failed resource by id, apply to all failed resources if id not provided")
    public ApiResp<Boolean> restoreFailed(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            resourceService.restoreFailedTaskById(id);
        } else {
            resourceService.restoreFailedTasks();
        }
        return ApiResp.success();
    }
}
