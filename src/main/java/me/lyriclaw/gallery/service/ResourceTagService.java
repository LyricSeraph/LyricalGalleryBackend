package me.lyriclaw.gallery.service;

import me.lyriclaw.gallery.dto.ResourceTagDTO;
import me.lyriclaw.gallery.entity.ResourceTag;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.repo.ResourceTagRepository;
import me.lyriclaw.gallery.throwable.ResponseException;
import me.lyriclaw.gallery.vo.ResourceTagQueryVO;
import me.lyriclaw.gallery.vo.ResourceTagUpdateVO;
import me.lyriclaw.gallery.vo.ResourceTagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceTagService {

    private final ResourceTagRepository resourceTagRepository;

    @Autowired
    public ResourceTagService(ResourceTagRepository resourceTagRepository) {
        this.resourceTagRepository = resourceTagRepository;
    }

    public Long save(ResourceTagVO vO) {
        ResourceTag bean = new ResourceTag();
        BeanUtils.copyProperties(vO, bean);
        bean = resourceTagRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        resourceTagRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, ResourceTagUpdateVO vO) {
        ResourceTag bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        resourceTagRepository.save(bean);
    }

    public ResourceTagDTO getById(Long id) {
        ResourceTag original = requireOne(id);
        return toDTO(original);
    }

    public Page<ResourceTagDTO> query(ResourceTagQueryVO vO, Pageable pageable) {
        ResourceTag queryItem = new ResourceTag();
        BeanUtils.copyProperties(vO, queryItem);
        return resourceTagRepository.findAll(Example.of(queryItem), pageable).map(this::toDTO);
    }

    public ResourceTagDTO toDTO(ResourceTag original) {
        ResourceTagDTO bean = new ResourceTagDTO();
        if (original != null) {
            BeanUtils.copyProperties(original, bean);
        }
        return bean;
    }

    private ResourceTag requireOne(Long id) {
        return resourceTagRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ApiResponseStatus.STATUS_NOT_FOUND, "ResourceTag not found: " + id));
    }
}
