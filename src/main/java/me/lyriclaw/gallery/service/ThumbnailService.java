package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfig;
import me.lyriclaw.gallery.constants.PreviewSize;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class ThumbnailService {

    private final StorageConfig storageConfig;
    private final List<ThumbnailGenerator> generators;

    @Autowired
    public ThumbnailService(StorageConfig storageConfig, List<ThumbnailGenerator> generators) {
        this.storageConfig = storageConfig;
        this.generators = generators;
    }

    boolean generateThumbnails(File file) {
        for (ThumbnailGenerator generator : generators) {
            if (generator.supportFile(file) && generator.generateThumbnails(file)) {
                return true;
            }
        }
        return false;
    }

    boolean deleteThumbnails(String filename) {
        for (PreviewSize size: PreviewSize.values()) {
            getPreviewFile(filename, size).delete();
        }
        return true;
    }

    public File getPreviewFile(String filename, PreviewSize size) {
        return Paths.get(storageConfig.getPreviewPath().toString(), filename + "_" + size.name() + ".png").toFile();
    }

}
