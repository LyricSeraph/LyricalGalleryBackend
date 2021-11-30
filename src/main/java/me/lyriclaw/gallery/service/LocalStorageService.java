package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfig;
import me.lyriclaw.gallery.constants.PreviewSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class LocalStorageService implements StorageService {

    private final ThumbnailService thumbnailService;
    private final StorageConfig storageConfig;

    @Autowired
    public LocalStorageService(ThumbnailService thumbnailService, StorageConfig storageConfig) {
        this.thumbnailService = thumbnailService;
        this.storageConfig = storageConfig;
    }

    public Path getPath(String filename) {
        return Paths.get(storageConfig.getResourcePath().toString(), filename);
    }

    @Override
    public boolean store(MultipartFile file, String filename) {
        File targetFile = getPath(filename).toFile();
        try {
            file.transferTo(targetFile);
            thumbnailService.generateThumbnails(targetFile);
        } catch (IOException e) {
            log.warn("LocalStorageService store throw exception", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean store(File localFile, String filename) {
        File targetFile = getPath(filename).toFile();
        try {
            FileCopyUtils.copy(localFile, targetFile);
            thumbnailService.generateThumbnails(targetFile);
        } catch (IOException e) {
            log.warn("LocalStorageService store throw exception", e);
            return false;
        } finally {
            localFile.delete();
        }
        return true;
    }

    @Override
    public String getUrl(String filename) {
        return "resources/" + filename;
    }

    @Override
    public void delete(String filename) {
        getPath(filename).toFile().delete();
        thumbnailService.deleteThumbnails(filename);
    }
}
