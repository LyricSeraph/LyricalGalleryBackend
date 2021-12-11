package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.ResourceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResourceTagRepository extends JpaRepository<ResourceTag, Long>, JpaSpecificationExecutor<ResourceTag> {

}