package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.ResourceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceTagRepository extends JpaRepository<ResourceTag, Long>, JpaSpecificationExecutor<ResourceTag> {

    @Modifying
    @Query("delete from ResourceTag r where r.resourceId = :resourceId and r.tagId = :tagId")
    void deleteByResourceIdAndTagId(@Param("resourceId") Long resourceId, @Param("tagId") Long tagId);
}