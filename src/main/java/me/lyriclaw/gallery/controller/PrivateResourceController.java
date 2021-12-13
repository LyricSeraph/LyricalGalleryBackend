package me.lyriclaw.gallery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import me.lyriclaw.gallery.constants.DownloadStatus;
import me.lyriclaw.gallery.constants.PreviewSize;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.entity.Resource;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
import me.lyriclaw.gallery.service.ResourceService;
import me.lyriclaw.gallery.service.StorageService;
import me.lyriclaw.gallery.utils.FilenameUtils;
import me.lyriclaw.gallery.vo.ApiResp;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.vo.ResourceDownloadVO;
import me.lyriclaw.gallery.vo.ResourceUpdateVO;
import me.lyriclaw.gallery.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.nio.file.Paths;
import java.util.UUID;

@Api(tags = "Private Resource APIs")
@Validated
@RestController
@RequestMapping("/private/api/resource")
@Slf4j
public class PrivateResourceController {

    private final ResourceService resourceService;
    private final StorageService storageService;

    @Autowired
    public PrivateResourceController(ResourceService resourceService, StorageService storageService, StorageConfigProps storageConfigProps) {
        this.resourceService = resourceService;
        this.storageService = storageService;
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete ")
    public ApiResp<Object> delete(@Valid @NotNull @PathVariable("id") Long id) {
        ResourceDTO resourceDTO = resourceService.getById(id);
        resourceService.delete(id);
        storageService.delete(resourceDTO.getStorageFilename());
        return ApiResp.success();
    }

    @PostMapping("/download")
    @ApiOperation("Download ")
    public ApiResp<ResourceDTO> download(@Valid @RequestBody ResourceDownloadVO vO) {
        if (StringUtils.hasLength(vO.getUrl())) {
            URI uri = URI.create(vO.getUrl());
            String originFilename = Paths.get(uri.getPath()).getFileName().toString();
            String extension = FilenameUtils.getExtension(originFilename);
            ResourceVO resourceVO = new ResourceVO();
            resourceVO.setUuid(UUID.randomUUID().toString());
            resourceVO.setExtension(extension);
            if (StringUtils.hasLength(vO.getName())) {
                resourceVO.setName(vO.getName());
            } else {
                resourceVO.setName(originFilename);
            }
            resourceVO.setSourceUrl(vO.getUrl());
            resourceVO.setAlbumId(vO.getAlbumId());
            resourceVO.setStatus(DownloadStatus.IDLE.getStatusCode());
            resourceVO.setRatio(1F);
            Long id = resourceService.save(resourceVO);
            return ApiResp.success(resourceService.getById(id));
        }
        return ApiResp.error(ApiResponseStatus.STATUS_PARAMETER_INVALID);
    }

    @PostMapping("/upload")
    @ApiOperation("Upload ")
    public ApiResp<ResourceDTO> upload(@RequestParam("file") MultipartFile file, @RequestParam(value = "album", required = false) Long albumId) {
        String originFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originFilename);
        ResourceVO resourceVO = new ResourceVO();
        resourceVO.setUuid(UUID.randomUUID().toString());
        resourceVO.setExtension(extension);
        resourceVO.setName(originFilename);
        resourceVO.setSourceUrl(null);
        resourceVO.setAlbumId(albumId);
        resourceVO.setRatio(1F);
        resourceVO.setStatus(DownloadStatus.FINISHED.getStatusCode());
        Long id = resourceService.save(resourceVO);
        ResourceDTO result = resourceService.getById(id);
        StorageService.StorageResult storageResult = storageService.store(file, result.getStorageFilename());
        if (storageResult.isSuccess()) {
            ThumbnailGenerator.GenerateThumbnailResult generateThumbnailResult = storageResult.getThumbnails();
            float ratio = generateThumbnailResult.getRatio();
            String sThumb = generateThumbnailResult.getThumbnails().get(PreviewSize.small);
            String mThumb = generateThumbnailResult.getThumbnails().get(PreviewSize.medium);
            String lThumb = generateThumbnailResult.getThumbnails().get(PreviewSize.large);
            resourceService.updateThumbnails(id, ratio, sThumb, mThumb, lThumb);
            return ApiResp.success(resourceService.getById(id));
        } else {
            return ApiResp.error(ApiResponseStatus.STATUS_STORAGE_ERROR);
        }
    }

    @PostMapping("/restore")
    @ApiOperation("Restore failed resource")
    public ApiResp<Boolean> restoreFailed(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            resourceService.restoreFailedTaskById(id);
        } else {
            resourceService.restoreFailedTasks();
        }
        return ApiResp.success();
    }
}
