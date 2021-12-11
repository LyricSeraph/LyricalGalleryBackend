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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ResourceDownloadScheduler {

    private final ResourceService resourceService;
    private final ResourceDownloader resourceDownloader;
    private final ThumbnailService thumbnailService;

    private int executionCount;

    public ResourceDownloadScheduler(ResourceService resourceService, ResourceDownloader resourceDownloader, ThumbnailService thumbnailService) {
        this.resourceService = resourceService;
        this.resourceDownloader = resourceDownloader;
        this.thumbnailService = thumbnailService;
        executionCount = 0;
    }

    @Scheduled(fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    void downloadSchedule() {
        log.debug("ResourceDownloadScheduler downloadSchedule: " + executionCount++);
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
                        if (r.isSuccess() && r.getThumbnails() != null) {
                            ThumbnailGenerator.GenerateThumbnailResult generateThumbnailResult = r.getThumbnails();
                            float ratio = generateThumbnailResult.getRatio();
                            String sThumb = generateThumbnailResult.getThumbnails().get(PreviewSize.small);
                            String mThumb = generateThumbnailResult.getThumbnails().get(PreviewSize.medium);
                            String lThumb = generateThumbnailResult.getThumbnails().get(PreviewSize.large);
                            resourceService.updateThumbnails(id, ratio, sThumb, mThumb, lThumb);
                            success = true;
                        }
                    } catch (InterruptedException | ExecutionException ignored) {}
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



    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    private void markFailedTasks() {
        resourceService.markFailedTasks();
    }

}
