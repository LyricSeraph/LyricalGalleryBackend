package me.lyriclaw.gallery.utils;

import me.lyriclaw.gallery.constants.PreviewSize;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Paths;

public class FilenameUtils {

    public static String getExtension(String filename) {
        String extension = "";
        if (StringUtils.hasLength(filename)) {
            int extensionIndex = filename.lastIndexOf(".");
            if (extensionIndex != -1) {
                extension = filename.substring(extensionIndex);
            }
        }
        return extension;
    }

}
