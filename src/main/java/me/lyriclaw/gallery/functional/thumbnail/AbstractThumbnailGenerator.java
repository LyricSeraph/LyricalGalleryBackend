package me.lyriclaw.gallery.functional.thumbnail;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfig;
import me.lyriclaw.gallery.constants.PreviewSize;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.springframework.data.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
public abstract class AbstractThumbnailGenerator implements ThumbnailGenerator {

    private final StorageConfig storageConfig;

    protected AbstractThumbnailGenerator(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

    @Override
    public boolean generateThumbnails(File file) {
        try {
            BufferedImage originImage = getFullSizeThumbnail(file);
            if (originImage != null) {
                for (PreviewSize size : PreviewSize.values()) {
                    generateThumbnail(getPreviewFilePath(file.getName(), size), originImage, size);
                }
                return true;
            }
        } catch (Exception e) {
            log.warn("ThumbnailService generateThumbnails throw exception", e);
        }
        return false;
    }

    abstract protected BufferedImage getFullSizeThumbnail(File file) throws IOException, ImageReadException;

    private File getPreviewFilePath(String filename, PreviewSize size) {
        return Paths.get(storageConfig.getPreviewPath().toString(), filename + "_" + size.name() + ".png").toFile();
    }

    private Pair<Integer, Integer> getScaleSize(int width, int height, int shortSize) {
        int scaleWidth = shortSize;
        int scaleHeight = shortSize;
        if (width < height) {
            scaleHeight = (int) (1.0f * height / width * scaleWidth);
        } else {
            scaleWidth = (int) (1.0f * width / height * scaleHeight);
        }
        return Pair.of(scaleWidth, scaleHeight);
    }

    private void generateThumbnail(File targetFile, BufferedImage image, PreviewSize size) throws ImageWriteException, IOException {
        int width = image.getWidth();
        int height = image.getHeight();
        Pair<Integer, Integer> resultWidthHeight = getScaleSize(width, height, size.getSize());
        int targetWidth = resultWidthHeight.getFirst();
        int targetHeight = resultWidthHeight.getSecond();
        Image scaleImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        outputImage.getGraphics().drawImage(scaleImage, 0, 0, null);
        Imaging.writeImage(outputImage, targetFile, ImageFormats.PNG, null);
    }




}
