package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {

    @Modifying
    @Query(nativeQuery = true,
            value = "update Resource set failed_tries = failed_tries + 1, status = 0 where id = :id")
    void updateDownloadFailed(@Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true,
            value = "update Resource set status = :status where id = :id")
    void updateDownloadResult(@Param("id") Long id, @Param("status") int status);

    @Modifying
    @Query(nativeQuery = true,
            value = "update Resource set " +
                    "ratio = :ratio, " +
                    "s_thumb = :sThumb, " +
                    "m_thumb = :mThumb, " +
                    "l_thumb = :lThumb " +
                    "where id = :id")
    void updateThumbnails(@Param("id") Long id,
                          @Param("ratio") Float ratio,
                          @Param("sThumb") String sThumb,
                          @Param("mThumb") String mThumb,
                          @Param("lThumb") String lThumb);

    @Modifying
    @Query(nativeQuery = true,
            value = "update Resource set status = 0 where status = 1")
    void resetDownloadingTasks();

    @Modifying
    @Query(nativeQuery = true,
            value = "update Resource set status = 3 where failed_tries > 10")
    void markFailedTasks();

    @Modifying
    @Query(nativeQuery = true,
            value = "update Resource set status = 0, failed_tries = 0 where status = 3")
    void restoreFailedTasks();

    @Modifying
    @Query(nativeQuery = true,
            value = "update Resource set status = 0, failed_tries = 0 where id = :id")
    void restoreFailedTaskById(@Param("id") @NonNull Long id);

    @Query(nativeQuery = true,
            value = "select unique " +
                    "Resource.id as id, " +
                    "Resource.uuid as uuid, " +
                    "Resource.extension as extension, " +
                    "Resource.name as name, " +
                    "Resource.source_url as source_url, " +
                    "Resource.album_id as album_id, " +
                    "Resource.failed_tries as failed_tries, " +
                    "Resource.status as status, " +
                    "Resource.ratio as ratio, " +
                    "Resource.s_thumb as s_thumb, " +
                    "Resource.m_thumb as m_thumb, " +
                    "Resource.l_thumb as l_thumb, " +
                    "Resource.created_at as created_at, " +
                    "Resource.updated_at as updated_at " +
                    "from Resource inner join ResourceTag on Resource.id = ResourceTag.resource_id " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:tagId is null or tag_id = :tagId) " +
                    "and (:name is null or name like %:name%) ",
            countQuery = "select unique count(*) " +
                    "from Resource inner join ResourceTag on Resource.id = resource_id " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:tagId is null or tag_id = :tagId) " +
                    "and (:name is null or name like %:name%) "
    )
    Page<Resource> findAllBy(@Param("albumId") Long albumId,
                             @Param("tagId") @NonNull Long tagId,
                             @Param("name") String name, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from Resource " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:name is null or name like %:name%)",
            countQuery = "select count(*) from Resource " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:name is null or name like %:name%)")
    Page<Resource> findAllBy(@Param("albumId") Long albumId,
                             @Param("name") String name, Pageable pageable);

}