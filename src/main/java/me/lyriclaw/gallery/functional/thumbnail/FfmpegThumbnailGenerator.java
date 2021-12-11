package me.lyriclaw.gallery.functional.thumbnail;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FfmpegThumbnailGenerator extends AbstractThumbnailGenerator {

    private static List<String> SUPPORTED_FORMATS = Arrays.asList(
            ".avi", ".mkv", ".mp4"
    );

    private final Random random;

    @Autowired
    protected FfmpegThumbnailGenerator(StorageConfigProps storageConfigProps) {
        super(storageConfigProps);
        random = new Random();
    }

    @Override
    protected BufferedImage getFullSizeThumbnail(File file) throws IOException, ImageReadException {
        float totalDuration = getVideoDuration(file);
        log.debug("FfmpegThumbnailGenerator getFullSizeThumbnail: " + "totalDuration = [" + totalDuration + "]");
        float takeThumbnailAt = (float) (totalDuration * random.nextDouble());
        log.debug("FfmpegThumbnailGenerator getFullSizeThumbnail: " + "takeThumbnailAt = [" + takeThumbnailAt + "]");
        String timeString = getTimeFormatOfSeconds(takeThumbnailAt);
        log.debug("FfmpegThumbnailGenerator getFullSizeThumbnail: " + "timeString = [" + timeString + "]");
        File generatedImageFile = generateThumbnailAt(file, timeString);
        log.debug("FfmpegThumbnailGenerator getFullSizeThumbnail: " + "generatedImageFile = [" + generatedImageFile + "]");
        if (generatedImageFile != null) {
            return Imaging.getBufferedImage(generatedImageFile);
        } else {
            return null;
        }
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

    private String getTimeFormatOfSeconds(float seconds) {
        int hour = ((int)(seconds / 3600));
        int minute = ((int)(seconds / 60));
        float sec = seconds - hour * 3600 - minute * 60;
        return String.format("%d:%d:%.2f", hour, minute, sec);
    }

    private float getVideoDuration(File file) {
        ProcessBuilder videoLengthRetriever = new ProcessBuilder("ffmpeg", "-i", file.getAbsolutePath());
        try {
            Process process = videoLengthRetriever.start();
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String durationString = "0:0:0.0";
            String line;
            while ((line = br.readLine()) != null) {
                log.debug("FfmpegThumbnailGenerator getVideoDuration: " + "line = [" + line + "]");
                if (line.contains("Duration")) {
                    Pattern p = Pattern.compile("\\d+:\\d+:\\d+\\.\\d+");
                    Matcher matcher = p.matcher(line);
                    if (matcher.find()) {
                        durationString = matcher.group();
                        break;
                    }
                }
            }
            process.waitFor();
            log.debug("FfmpegThumbnailGenerator getVideoDuration: " + "durationString = [" + durationString + "]");
            List<Float> collect = Arrays.stream(durationString.split(":")).map(Float::parseFloat).collect(Collectors.toList());
            return collect.get(0) * 60 * 60 + collect.get(1) * 60 + collect.get(2);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private File generateThumbnailAt(File file, String timeString) {
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
            ProcessBuilder videoThumbnailExtractor = new ProcessBuilder(
                    "ffmpeg", "-i", file.getAbsolutePath(), "-ss", timeString, "-frames", "1", "-y", tmpFile.getAbsolutePath());
            Process process = videoThumbnailExtractor.start();
            InputStream stderr = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                log.debug("FfmpegThumbnailGenerator generateThumbnailAt: " + "line = [" + line + "]");
            }
            process.waitFor();
            return tmpFile;
        } catch (IOException | InterruptedException e) {
            log.warn("FfmpegThumbnailGenerator generateThumbnailAt throw exception", e);
            tmpFile.delete();
        }
        return null;
    }

}
