package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.dto.TagDTO;
import me.lyriclaw.gallery.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    @Query(nativeQuery = true,
            value = "select * from Tag " +
                    "where (:name is null or name like %:name%) " +
                    "and tag_id in " +
                    "(select unique(tag_id) " +
                    "from ResourceTag inner join Resource on ResourceTag.resource_id = Resource.resource_id " +
                    "where album_id = :albumId)")
    List<Tag> findTagsByAlbumAndName(@Param("albumId") Long albumId, @Param("name") String name);
}