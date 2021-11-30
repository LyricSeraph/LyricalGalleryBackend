package me.lyriclaw.gallery.functional.thumbnail;

import java.io.File;

public interface ThumbnailGenerator {

    boolean supportFile(File file);
    boolean generateThumbnails(File file);

}
