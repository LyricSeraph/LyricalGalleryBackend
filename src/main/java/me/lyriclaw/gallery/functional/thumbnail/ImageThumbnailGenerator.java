package me.lyriclaw.gallery.functional.thumbnail;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfig;
import me.lyriclaw.gallery.constants.PreviewSize;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class ImageThumbnailGenerator extends AbstractThumbnailGenerator {

    private static List<String> SUPPORTED_FORMATS = Arrays.asList(
            ".png", ".jpg", ".jpeg", ".bmp", ".gif"
    );

    @Autowired
    public ImageThumbnailGenerator(StorageConfig storageConfig) {
        super(storageConfig);
    }

    @Override
    protected BufferedImage getFullSizeThumbnail(File file) throws IOException, ImageReadException {
        return Imaging.getBufferedImage(file);
    }

    @Override
    public boolean supportFile(File file) {
        for (String supportExtension : SUPPORTED_FORMATS) {
            if (file.getName().endsWith(supportExtension)) {
                return true;
            }
        }
        return false;
    }


}
