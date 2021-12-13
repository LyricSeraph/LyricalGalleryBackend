package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import me.lyriclaw.gallery.dto.ResourceDTO;
import me.lyriclaw.gallery.functional.downloader.HttpUrlDownloader;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Component
@Slf4j
public class ResourceDownloader {

    private final ApplicationContext context;
    private final ResourceService resourceService;
    private final StorageService storageService;

    @Autowired
    public ResourceDownloader(ApplicationContext context, ResourceService resourceService, StorageService storageService, StorageConfigProps storageConfigProps) {
        this.context = context;
        this.resourceService = resourceService;
        this.storageService = storageService;
    }

    @PostConstruct
    public void cleanDirtyData() {
        resourceService.resetDirtyDownloadTasks();
    }

    @Async
    public Future<StorageService.StorageResult> downloadResource(ResourceDTO resourceDTO) {
        log.debug("DownloadExecutor executeTask: " + "resourceDTO = [" + resourceDTO + "]");
        OkHttpClient client = context.getBean(OkHttpClient.class);
        try {
            File tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
            log.debug("ResourceDownloader downloadResource: " + "tempFile = [" + tempFile + "]");
            HttpUrlDownloader downloader = new HttpUrlDownloader(resourceDTO.getSourceUrl(), tempFile);
            if (downloader.download(client)) {
                log.debug("ResourceDownloader downloadResource: download success: " + resourceDTO.getSourceUrl());
                return AsyncResult.forValue(storageService.store(downloader.getSavedFile(), resourceDTO.getStorageFilename()));
            }
        } catch (Exception e) {
            log.warn("DownloadExecutor executeTask throw exception", e);
        }
        return AsyncResult.forValue(null);
    }

}
