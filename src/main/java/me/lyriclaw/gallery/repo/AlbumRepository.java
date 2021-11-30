package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {

}