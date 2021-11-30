package me.lyriclaw.gallery.service;

import me.lyriclaw.gallery.dto.ResourceTagDTO;
import me.lyriclaw.gallery.dto.TagDTO;
import me.lyriclaw.gallery.entity.Tag;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.repo.TagRepository;
import me.lyriclaw.gallery.throwable.ResponseException;
import me.lyriclaw.gallery.vo.TagQueryVO;
import me.lyriclaw.gallery.vo.TagUpdateVO;
import me.lyriclaw.gallery.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final ResourceTagService resourceTagService;

    @Autowired
    public TagService(TagRepository tagRepository, ResourceTagService resourceTagService) {
        this.tagRepository = tagRepository;
        this.resourceTagService = resourceTagService;
    }

    public Long save(TagVO vO) {
        Tag bean = new Tag();
        BeanUtils.copyProperties(vO, bean);
        bean = tagRepository.save(bean);
        return bean.getId();
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, TagUpdateVO vO) {
        Tag bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        tagRepository.save(bean);
    }

    public TagDTO getById(Long id) {
        Tag original = requireOne(id);
        return toDTO(original);
    }

    public Page<TagDTO> query(TagQueryVO vO, Pageable pageable) {
        Tag queryItem = new Tag();
        BeanUtils.copyProperties(vO, queryItem);
        return tagRepository.findAll(Example.of(queryItem), pageable).map(this::toDTO);
    }

    public Page<TagDTO> queryByResourceId(Long tagId, Pageable pageable) {
        Page<ResourceTagDTO> page = resourceTagService.findAllByTagId(tagId, pageable);
        List<Long> ids = page.map(ResourceTagDTO::getTagId)
                .stream()
                .collect(Collectors.toList());
        final Map<Long, Tag> tagMap = tagRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(Tag::getId, (r) -> r));
        return page.map(rt -> toDTO(tagMap.getOrDefault(rt.getResourceId(), null)));
    }

    private TagDTO toDTO(Tag original) {
        TagDTO bean = new TagDTO();
        if (original != null) {
            BeanUtils.copyProperties(original, bean);
        }
        return bean;
    }

    private Tag requireOne(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ApiResponseStatus.STATUS_NOT_FOUND, "Tag not found: " + id));
    }
}
