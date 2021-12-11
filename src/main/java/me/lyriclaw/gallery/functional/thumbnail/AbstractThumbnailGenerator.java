package me.lyriclaw.gallery.functional.thumbnail;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import me.lyriclaw.gallery.constants.PreviewSize;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.springframework.data.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Slf4j
public abstract class AbstractThumbnailGenerator implements ThumbnailGenerator {

    private final StorageConfigProps storageConfigProps;

    protected AbstractThumbnailGenerator(StorageConfigProps storageConfigProps) {
        this.storageConfigProps = storageConfigProps;
    }

    @Override
    public GenerateThumbnailResult generateThumbnails(File file) {
        Map<PreviewSize, String> resultMap = new HashMap<>();
        BufferedImage originImage;
        float ratio;
        try {
            originImage = getFullSizeThumbnail(file);
            ratio = 1.0f * originImage.getWidth() / originImage.getHeight();
        } catch (Exception e) {
            log.debug("AbstractThumbnailGenerator generateThumbnails throw exception", e);
            return null;
        }
        for (PreviewSize size : PreviewSize.values()) {
            File targetFile = getPreviewFilePath(file.getName(), size);
            if (generateThumbnail(targetFile, originImage, size)) {
                resultMap.put(size, targetFile.getName());
            }
        }
        return new GenerateThumbnailResult(ratio, resultMap);
    }

    abstract protected BufferedImage getFullSizeThumbnail(File file) throws IOException, ImageReadException;

    private File getPreviewFilePath(String filename, PreviewSize size) {
        return Paths.get(storageConfigProps.getThumbnailPath().toString(), filename + "_" + size.name() + ".png").toFile();
    }

    private Pair<Integer, Integer> getScaleSize(int width, int height, int heightSize) {
        int scaleWidth = (int) (1.0f * width / height * heightSize);
        return Pair.of(scaleWidth, heightSize);
    }

    private boolean generateThumbnail(File targetFile, BufferedImage image, PreviewSize size) {
        try {
            int width = image.getWidth();
            int height = image.getHeight();
            Pair<Integer, Integer> resultWidthHeight = getScaleSize(width, height, size.getSize());
            int targetWidth = resultWidthHeight.getFirst();
            int targetHeight = resultWidthHeight.getSecond();
            Image scaleImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            outputImage.getGraphics().drawImage(scaleImage, 0, 0, null);
            Imaging.writeImage(outputImage, targetFile, ImageFormats.PNG, null);
        } catch (Exception e) {
            log.debug("AbstractThumbnailGenerator generateThumbnail throw exception", e);
            return false;
        }
        return true;
    }




}
