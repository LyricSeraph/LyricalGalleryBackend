package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfig;
import me.lyriclaw.gallery.constants.DownloadStatus;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.functional.downloader.HttpUrlDownloader;
import me.lyriclaw.gallery.utils.FilenameUtils;
import me.lyriclaw.gallery.vo.ResourceVO;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.UUID;

@Component
@Slf4j
public class DownloadExecutor {

    private final ApplicationContext context;
    private final ResourceService resourceService;
    private final StorageService storageService;

    @Autowired
    public DownloadExecutor(ApplicationContext context, ResourceService resourceService, StorageService storageService, StorageConfig storageConfig) {
        this.context = context;
        this.resourceService = resourceService;
        this.storageService = storageService;
    }

    @PostConstruct
    public void cleanDirtyData() {
        resourceService.resetDirtyDownloadTasks();
    }

    @Async
    public void executeTask(ResourceDTO resourceDTO) {
        log.debug("DownloadExecutor executeTask: " + "resourceDTO = [" + resourceDTO + "]");
        resourceService.updateStatusById(resourceDTO.getId(), DownloadStatus.DOWNLOADING);
        OkHttpClient client = context.getBean(OkHttpClient.class);
        boolean saveSuccess = false;
        try {
            File tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
            HttpUrlDownloader downloader = new HttpUrlDownloader(resourceDTO.getSourceUrl(), tempFile);
            if (downloader.download(client) && storageService.store(downloader.getSavedFile(), resourceDTO.getStorageFilename())) {
                tempFile.delete();
                resourceService.updateStatusById(resourceDTO.getId(), DownloadStatus.FINISHED);
                saveSuccess = true;
            }
        } catch (Exception e) {
            log.warn("DownloadExecutor executeTask throw exception", e);
        }
        log.debug("DownloadExecutor executeTask: " + "saveSuccess = [" + saveSuccess + "]");
        if (!saveSuccess) {
            resourceService.updateStatusById(resourceDTO.getId(), DownloadStatus.FAILED);
        }
    }


}