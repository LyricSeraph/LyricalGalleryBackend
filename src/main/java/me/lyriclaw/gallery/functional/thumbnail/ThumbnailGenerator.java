package me.lyriclaw.gallery.functional.thumbnail;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.lyriclaw.gallery.constants.PreviewSize;

import java.io.File;
import java.util.Map;

public interface ThumbnailGenerator {

    @Data
    @AllArgsConstructor
    class GenerateThumbnailResult {
        /**
         * Thumbnail ratio
         */
        float ratio;
        /**
         * generated filenames
         */
        Map<PreviewSize, String> thumbnails;
    }

    boolean supportFile(File file);
    GenerateThumbnailResult generateThumbnails(File file) throws Exception;

}
