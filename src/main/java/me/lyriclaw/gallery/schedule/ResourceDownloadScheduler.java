package me.lyriclaw.gallery.schedule;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.constants.DownloadStatus;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.service.DownloadExecutor;
import me.lyriclaw.gallery.service.ResourceService;
import me.lyriclaw.gallery.vo.ResourceQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class ResourceDownloadScheduler {

    @Autowired
    ResourceService resourceService;

    @Autowired
    DownloadExecutor downloadExecutor;

    @Scheduled(cron = "*/10 * * * * *")
    private void downloadSchedule() {
        int page = 0;
        Page<ResourceDTO> tasks;
        do {
            ResourceQueryVO query = new ResourceQueryVO();
            query.setStatus(DownloadStatus.IDLE.getStatusCode());
            tasks = resourceService.query(query, PageRequest.of(page++, 5)
                            .withSort(Sort.by(Sort.Direction.ASC, "updatedAt", "createdAt")));
            if (!tasks.isEmpty()) {
                log.debug("ResourceDownloadScheduler downloadSchedule: " + "tasks: " + tasks.getContent());
                for (ResourceDTO resourceDTO : tasks) {
                    downloadExecutor.executeTask(resourceDTO);
                }
            }
        } while (!tasks.isEmpty());
    }

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    private void markFailedTasks() {
        resourceService.markFailedTasks();
    }

}
