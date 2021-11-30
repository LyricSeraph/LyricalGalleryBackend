package me.lyriclaw.gallery.repo;

import me.lyriclaw.gallery.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
            value = "update Resource set status = 0 where status = 1")
    void resetDownloadingTasks();


}