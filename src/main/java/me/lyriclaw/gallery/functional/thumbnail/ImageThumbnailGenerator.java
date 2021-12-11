package me.lyriclaw.gallery.functional.thumbnail;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public ImageThumbnailGenerator(StorageConfigProps storageConfigProps) {
        super(storageConfigProps);
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
