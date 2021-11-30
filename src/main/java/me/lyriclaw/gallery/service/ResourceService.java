package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.constants.DownloadStatus;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.dto.ResourceTagDTO;
import me.lyriclaw.gallery.entity.Resource;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
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

import java.util.List;
import java.util.Map;
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
        return bean.getId();
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


    public ResourceDTO getById(Long id) {
        Resource original = requireOne(id);
        return toDTO(original);
    }

    public Page<ResourceDTO> query(ResourceQueryVO vO, Pageable pageable) {
        Resource queryItem = new Resource();
        BeanUtils.copyProperties(vO, queryItem);
        return resourceRepository.findAll(Example.of(queryItem), pageable).map(this::toDTO);
    }

    public Page<ResourceDTO> queryByAlbumAndTagAndName(@Nullable Long albumId, @Nullable Long tagId, @Nullable String name, Pageable pageable) {
        Page<Resource> page = resourceRepository.findAllBy(albumId, tagId, name, pageable);
        return page.map(this::toDTO);
    }

    private ResourceDTO toDTO(Resource original) {
        ResourceDTO bean = new ResourceDTO();
        if (original != null) {
            BeanUtils.copyProperties(original, bean);
            bean.setTags(original.getTags().stream().map(resourceTagService::toDTO).collect(Collectors.toList()));
        }
        return bean;
    }

    private Resource requireOne(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ApiResponseStatus.STATUS_NOT_FOUND, "Resource not found: " + id));
    }
}
