package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.constants.DownloadStatus;
import me.lyriclaw.gallery.constants.PreviewSize;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.entity.Resource;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
import me.lyriclaw.gallery.repo.ResourceRepository;
import me.lyriclaw.gallery.throwable.ResponseException;
import me.lyriclaw.gallery.vo.ResourceQueryVO;
import me.lyriclaw.gallery.vo.ResourceUpdateVO;
import me.lyriclaw.gallery.vo.ResourceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceTagService resourceTagService;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository, ResourceTagService resourceTagService) {
        this.resourceRepository = resourceRepository;
        this.resourceTagService = resourceTagService;
    }

    public Long save(ResourceVO vO) {
        Resource bean = new Resource();
        BeanUtils.copyProperties(vO, bean);
        bean = resourceRepository.save(bean);
        return bean.getResourceId();
    }

    public void delete(Long id) {
        resourceRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, ResourceUpdateVO vO) {
        Resource bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        resourceRepository.save(bean);
    }

    @Transactional
    public void updateStatusById(Long id, @NonNull DownloadStatus status) {
        log.debug("DownloadTaskService updateStatusById: " + "id = [" + id + "], status = [" + status + "]");
        if (status == DownloadStatus.FAILED) {
            resourceRepository.updateDownloadFailed(id);
        } else {
            resourceRepository.updateDownloadResult(id, status.getStatusCode());
        }
    }

    @Transactional
    public void resetDirtyDownloadTasks() {
        resourceRepository.resetDownloadingTasks();
    }

    @Transactional
    public void restoreFailedTasks() {
        resourceRepository.restoreFailedTasks();
    }

    @Transactional
    public void restoreFailedTaskById(@NonNull Long id) {
        resourceRepository.restoreFailedTaskById(id);
    }


    @Transactional
    public void updateResourceThumbnails(Long id, ThumbnailGenerator.GenerateThumbnailResult thumbnailResult) {
        float ratio = thumbnailResult.getRatio();
        String sThumb = thumbnailResult.getThumbnails().get(PreviewSize.small);
        String mThumb = thumbnailResult.getThumbnails().get(PreviewSize.medium);
        String lThumb = thumbnailResult.getThumbnails().get(PreviewSize.large);
        updateThumbnails(id, ratio, sThumb, mThumb, lThumb);
    }

    public void updateThumbnails(@NonNull Long id, float thumbRatio, String sThumb, String mThumb, String lThumb) {
        resourceRepository.updateThumbnails(id, thumbRatio, sThumb, mThumb, lThumb);
    }

    public ResourceDTO getById(Long id) {
        Resource original = requireOne(id);
        return toDTO(original);
    }

    public Page<ResourceDTO> query(ResourceQueryVO vO, Pageable pageable) {
        Resource queryItem = new Resource();
        BeanUtils.copyProperties(vO, queryItem);
        return resourceRepository.findAll(Example.of(queryItem), pageable).map(this::toDTO);
    }


    public Page<ResourceDTO> findAll(@Nullable Long tagId, @Nullable String name, @Nullable Integer status, Pageable pageable) {
        Page<Resource> page;
        if (tagId != null) {
            page = resourceRepository.findAllBy(tagId, name, status, pageable);
        } else {
            page = resourceRepository.findAllBy(name, status, pageable);
        }
        return page.map(this::toDTO);
    }

    public Page<ResourceDTO> findInAlbum(@Nullable Long albumId, @Nullable Long tagId, @Nullable String name, @Nullable Integer status, Pageable pageable) {
        Page<Resource> page;
        if (tagId != null) {
            page = resourceRepository.findAllInAlbumBy(albumId, tagId, name, status, pageable);
        } else {
            page = resourceRepository.findAllInAlbumBy(albumId, name, status, pageable);
        }
        return page.map(this::toDTO);
    }

    private ResourceDTO toDTO(Resource original) {
        ResourceDTO bean = new ResourceDTO();
        if (original != null) {
            BeanUtils.copyProperties(original, bean);
            if (original.getTags() != null) {
                bean.setTags(original.getTags().stream().map(resourceTagService::toDTO).collect(Collectors.toList()));
            }
        }
        return bean;
    }

    public Resource requireOne(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ApiResponseStatus.STATUS_NOT_FOUND, "Resource not found: " + id));
    }
}
