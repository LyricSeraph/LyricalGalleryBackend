package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {

    @Query(nativeQuery = true,
            value = "select * from Album " +
                    "where ((:parentId is null and parent_id is null) or parent_id = :parentId)" +
                    "and (:name is null or name like %:name%)",
            countQuery = "select count(*) from Album " +
                    "where ((:parentId is null and parent_id is null) or parent_id = :parentId)" +
                    "and (:name is null or name like %:name%")
    Page<Album> findAllByParentIdAndNameLike(@Nullable @Param("parentId") Long parentId, @Nullable @Param("name") String name, Pageable pageable);

}