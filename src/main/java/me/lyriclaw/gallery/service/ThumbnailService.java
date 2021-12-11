package me.lyriclaw.gallery.service;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import me.lyriclaw.gallery.constants.PreviewSize;
import me.lyriclaw.gallery.functional.thumbnail.ThumbnailGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Service
@Slf4j
public class ThumbnailService {

    private final StorageConfigProps storageConfigProps;
    private final List<ThumbnailGenerator> generators;

    @Autowired
    public ThumbnailService(StorageConfigProps storageConfigProps, List<ThumbnailGenerator> generators) {
        this.storageConfigProps = storageConfigProps;
        this.generators = generators;
    }

    /**
     * Generate thunbnails for given file.
     * @param file
     *      The given file.
     * @return
     *      The generated result. Return null when generate failed.
     */
    public ThumbnailGenerator.GenerateThumbnailResult generateThumbnails(File file) {
        for (ThumbnailGenerator generator : generators) {
            if (generator.supportFile(file)) {
                try {
                    return generator.generateThumbnails(file);
                } catch (Exception e) {
                    log.debug("ThumbnailService generateThumbnails throw exception", e);
                }
            }
        }
        return null;
    }

    boolean deleteThumbnails(String filename) {
        for (PreviewSize size: PreviewSize.values()) {
            getPreviewFile(filename, size).delete();
        }
        return true;
    }

    public File getPreviewFile(String filename, PreviewSize size) {
        return Paths.get(storageConfigProps.getThumbnailPath().toString(), filename + "_" + size.name() + ".png").toFile();
    }

}
