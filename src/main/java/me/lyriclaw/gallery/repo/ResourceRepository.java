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

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "update Resource set failed_tries = failed_tries + 1, status = 0 where resource_id = :id")
    void updateDownloadFailed(@Param("id") Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "update Resource set status = :status where resource_id = :id")
    void updateDownloadResult(@Param("id") Long id, @Param("status") int status);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "update Resource set " +
                    "ratio = :ratio, " +
                    "s_thumb = :sThumb, " +
                    "m_thumb = :mThumb, " +
                    "l_thumb = :lThumb " +
                    "where resource_id = :id")
    void updateThumbnails(@Param("id") Long id,
                          @Param("ratio") Float ratio,
                          @Param("sThumb") String sThumb,
                          @Param("mThumb") String mThumb,
                          @Param("lThumb") String lThumb);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "update Resource set status = 0 where status = 1")
    void resetDownloadingTasks();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "update Resource set status = 3 where failed_tries > 10")
    void markFailedTasks();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "update Resource set status = 0, failed_tries = 0 where status = 3")
    void restoreFailedTasks();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(nativeQuery = true,
            value = "update Resource set status = 0, failed_tries = 0 where resource_id = :id")
    void restoreFailedTaskById(@Param("id") @NonNull Long id);

    @Query(nativeQuery = true,
            value = "select * " +
                    "from Resource r inner join ResourceTag rt on r.resource_id = rt.resource_id " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:tagId is null or tag_id = :tagId) " +
                    "and (:status is null or status = :status) " +
                    "and (:name is null or name like %:name%) ",
            countQuery = "select count(*) " +
                    "from Resource inner join ResourceTag on Resource.resource_id = ResourceTag.resource_id " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:tagId is null or tag_id = :tagId) " +
                    "and (:status is null or status = :status) " +
                    "and (:name is null or name like %:name%) "
    )
    Page<Resource> findAllBy(@Param("albumId") Long albumId,
                             @Param("tagId") @NonNull Long tagId,
                             @Param("name") String name,
                             @Param("status") Integer status,
                             Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from Resource " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:status is null or status = :status) " +
                    "and (:name is null or name like %:name%)",
            countQuery = "select count(*) from Resource " +
                    "where (:albumId is null or album_id = :albumId) " +
                    "and (:status is null or status = :status) " +
                    "and (:name is null or name like %:name%)")
    Page<Resource> findAllBy(@Param("albumId") Long albumId,
                             @Param("name") String name,
                             @Param("status") Integer status,
                             Pageable pageable);

}