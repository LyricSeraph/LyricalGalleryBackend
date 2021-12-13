package me.lyriclaw.gallery.service;

import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.dto.TagDTO;
import me.lyriclaw.gallery.entity.Tag;
import me.lyriclaw.gallery.repo.TagRepository;
import me.lyriclaw.gallery.throwable.ResponseException;
import me.lyriclaw.gallery.vo.TagQueryVO;
import me.lyriclaw.gallery.vo.TagUpdateVO;
import me.lyriclaw.gallery.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Long save(TagVO vO) {
        Tag bean = new Tag();
        BeanUtils.copyProperties(vO, bean);
        bean = tagRepository.save(bean);
        return bean.getTagId();
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

    public List<TagDTO> query(TagQueryVO vO) {
        Tag queryItem = new Tag();
        BeanUtils.copyProperties(vO, queryItem);
        return tagRepository.findAll(Example.of(queryItem))
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<TagDTO> findTagsByAlbumAndName(Long albumId, String name) {
        return tagRepository.findTagsByAlbumAndName(albumId, name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TagDTO toDTO(Tag original) {
        TagDTO bean = new TagDTO();
        if (original != null) {
            BeanUtils.copyProperties(original, bean);
        }
        return bean;
    }

    public Tag requireOne(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ApiResponseStatus.STATUS_NOT_FOUND, "Tag not found: " + id));
    }

}
