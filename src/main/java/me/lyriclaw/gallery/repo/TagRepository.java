package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.dto.TagDTO;
import me.lyriclaw.gallery.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    @Query(nativeQuery = true,
            value = "select * from ResourceTag inner join Resource on ResourceTag.resource_id = Resource.resource_id")
    List<TagDTO> findAllByAlbumId(Long albumId);
}