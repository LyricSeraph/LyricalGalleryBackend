package me.lyriclaw.gallery.service;

import me.lyriclaw.gallery.dto.AlbumDTO;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.entity.Album;
import me.lyriclaw.gallery.constants.ApiResponseStatus;
import me.lyriclaw.gallery.entity.Resource;
import me.lyriclaw.gallery.repo.AlbumRepository;
import me.lyriclaw.gallery.throwable.ResponseException;
import me.lyriclaw.gallery.vo.AlbumQueryVO;
import me.lyriclaw.gallery.vo.AlbumUpdateVO;
import me.lyriclaw.gallery.vo.AlbumVO;
import me.lyriclaw.gallery.vo.ResourceQueryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ResourceService resourceService;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, ResourceService resourceService) {
        this.albumRepository = albumRepository;
        this.resourceService = resourceService;
    }

    public Long save(AlbumVO vO) {
        Album bean = new Album();
        BeanUtils.copyProperties(vO, bean);
        bean = albumRepository.save(bean);
        return bean.getAlbumId();
    }

    public void delete(Long id) {
        albumRepository.deleteById(id);
    }

    @Transactional
    public void update(Long id, AlbumUpdateVO vO) {
        Album bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        albumRepository.save(bean);
    }

    public AlbumDTO getById(Long id) {
        Album original = requireOne(id);
        return fillResourceData(toDTO(original));
    }

    public Page<AlbumDTO> query(AlbumQueryVO vO, Pageable pageable) {
        Album queryItem = new Album();
        BeanUtils.copyProperties(vO, queryItem);
        return albumRepository.findAll(Example.of(queryItem), pageable)
                .map(this::toDTO)
                .map(this::fillResourceData);
    }


    public Page<AlbumDTO> findByNameLike(String name, Pageable pageable) {
        return albumRepository.findAllByNameLike(name, pageable)
                .map(this::toDTO)
                .map(this::fillResourceData);
    }

    private AlbumDTO fillResourceData(AlbumDTO albumDTO) {
        ResourceQueryVO queryVO = new ResourceQueryVO();
        queryVO.setAlbumId(albumDTO.getAlbumId());
        Page<ResourceDTO> sampleImages = resourceService.query(queryVO,
                PageRequest.ofSize(4).withSort(Sort.Direction.DESC, "resourceId"));
        albumDTO.setSampleResources(sampleImages.toList());
        albumDTO.setAlbumSize(sampleImages.getTotalElements());
        return albumDTO;
    }

    private AlbumDTO toDTO(Album original) {
        AlbumDTO bean = new AlbumDTO();
        if (original != null) {
            BeanUtils.copyProperties(original, bean);
        }
        return bean;
    }

    private Album requireOne(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new ResponseException(ApiResponseStatus.STATUS_NOT_FOUND, "Album not found: " + id));
    }
}
