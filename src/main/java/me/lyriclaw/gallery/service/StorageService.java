package me.lyriclaw.gallery.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.lyriclaw.gallery.constants.PreviewSize;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

public interface StorageService {

    @Data
    @AllArgsConstructor
    class StorageResult {
        boolean success;
        ThumbnailGenerator.GenerateThumbnailResult thumbnails;
    }

    StorageResult store(MultipartFile file, String filename);

    StorageResult store(File localFile, String filename);

    void delete(String filename);

    String getUrl(String filename);

}