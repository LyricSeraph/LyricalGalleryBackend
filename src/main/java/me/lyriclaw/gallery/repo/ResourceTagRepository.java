package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.ResourceTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceTagRepository extends JpaRepository<ResourceTag, Long>, JpaSpecificationExecutor<ResourceTag> {

    @Query(nativeQuery = true,
            value = "select ResourceTag.id as id," +
                    "       ResourceTag.tag_id as tag_id," +
                    "       ResourceTag.resource_id as resource_id," +
                    "       Resource.name as name," +
                    "       Resource.created_at as created_at," +
                    "       Resource.updated_at as updated_at " +
                    "from ResourceTag inner join Resource on resource_id = Resource.id " +
                    "where tag_id = :tagId",
            countQuery = "select count(*) from ResourceTag where tag_id = :tagId")
    Page<ResourceTag> findAllByTagId(@Param("tagId") Long tagId, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select ResourceTag.id as id," +
                    "       ResourceTag.tag_id as tag_id," +
                    "       ResourceTag.resource_id as resource_id," +
                    "       Resource.name as name," +
                    "       Resource.created_at as created_at," +
                    "       Resource.updated_at as updated_at " +
                    "from ResourceTag inner join Resource on resource_id = Resource.id " +
                    "where resource_id = :resourceId",
            countQuery = "select count(*) from ResourceTag where resource_id = :resource_id")
    Page<ResourceTag> findAllByResourceId(@Param("resourceId") Long resourceId, Pageable pageable);

}