package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {

    @Query(nativeQuery = true,
            value = "select * from Album where name like %:name%")
    Page<Album> findAllByNameLike(@Param("name") String name, Pageable pageable);
}