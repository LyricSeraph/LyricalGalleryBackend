package me.lyriclaw.gallery.schedule;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.constants.DownloadStatus;
import me.lyriclaw.gallery.constants.PreviewSize;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
import me.lyriclaw.gallery.service.ResourceDownloader;
import me.lyriclaw.gallery.service.ResourceService;
import me.lyriclaw.gallery.service.StorageService;
import me.lyriclaw.gallery.service.ThumbnailService;
import me.lyriclaw.gallery.vo.ResourceQueryVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ResourceDownloadScheduler {

    private final ResourceService resourceService;
    private final ResourceDownloader resourceDownloader;

    public ResourceDownloadScheduler(ResourceService resourceService, ResourceDownloader resourceDownloader, ThumbnailService thumbnailService) {
        this.resourceService = resourceService;
        this.resourceDownloader = resourceDownloader;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    void downloadSchedule() {
        Page<ResourceDTO> tasks;
        do {
            tasks = getIdleTasks();
            if (!tasks.isEmpty()) {
                log.debug("ResourceDownloadScheduler downloadSchedule: " + "tasks: " + tasks.getContent());
                Map<Long, Future<StorageService.StorageResult>> results = new HashMap<>();
                for (ResourceDTO resourceDTO : tasks) {
                    resourceService.updateStatusById(resourceDTO.getResourceId(), DownloadStatus.DOWNLOADING);
                    results.put(resourceDTO.getResourceId(), resourceDownloader.downloadResource(resourceDTO));
                }
                for (Map.Entry<Long, Future<StorageService.StorageResult>> entry : results.entrySet()) {
                    boolean success = false;
                    Long id = entry.getKey();
                    Future<StorageService.StorageResult> future = entry.getValue();
                    try {
                        StorageService.StorageResult r = future.get();
                        if (r != null && r.getThumbnails() != null) {
                            resourceService.updateResourceThumbnails(id, r.getThumbnails());
                        }
                        if (r != null && r.isSuccess()) {
                            success = true;
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        log.debug("ResourceDownloadScheduler downloadSchedule throw exception", e);
                    }
                    resourceService.updateStatusById(id, success ? DownloadStatus.FINISHED : DownloadStatus.FAILED);
                }
            }
        } while (!tasks.isEmpty());
    }

    private Page<ResourceDTO> getIdleTasks() {
        ResourceQueryVO query = new ResourceQueryVO();
        query.setStatus(DownloadStatus.IDLE.getStatusCode());
        return resourceService.query(query, PageRequest.of(0, 5)
                .withSort(Sort.by(Sort.Direction.ASC, "updatedAt", "createdAt")));
    }

}
