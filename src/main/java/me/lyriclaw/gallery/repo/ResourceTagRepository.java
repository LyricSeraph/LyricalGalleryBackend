package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.ResourceTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceTagRepository extends JpaRepository<ResourceTag, Long>, JpaSpecificationExecutor<ResourceTag> {

}