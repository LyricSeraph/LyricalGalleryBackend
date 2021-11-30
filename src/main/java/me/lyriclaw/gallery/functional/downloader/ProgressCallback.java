package me.lyriclaw.gallery.functional.downloader;

public interface ProgressCallback {
    void onProgress(long current, long total);
}
