package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
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
    private final StorageConfigProps storageConfigProps;

    @Autowired
    public LocalStorageService(ThumbnailService thumbnailService, StorageConfigProps storageConfigProps) {
        this.thumbnailService = thumbnailService;
        this.storageConfigProps = storageConfigProps;
    }

    public Path getPath(String filename) {
        return Paths.get(storageConfigProps.getResourcePath().toString(), filename);
    }

    @Override
    public StorageResult store(MultipartFile file, String filename) {
        File targetFile = getPath(filename).toFile();
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            log.warn("LocalStorageService store throw exception", e);
            return new StorageResult(false, null);
        }
        ThumbnailGenerator.GenerateThumbnailResult result = thumbnailService.generateThumbnails(targetFile);
        return new StorageResult(true, result);
    }

    @Override
    public StorageResult store(File localFile, String filename) {
        File targetFile = getPath(filename).toFile();
        try {
            FileCopyUtils.copy(localFile, targetFile);
        } catch (IOException e) {
            log.warn("LocalStorageService store throw exception", e);
            return new StorageResult(false, null);
        } finally {
            localFile.delete();
        }
        ThumbnailGenerator.GenerateThumbnailResult result = thumbnailService.generateThumbnails(targetFile);
        return new StorageResult(true, result);
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
