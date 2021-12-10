package me.lyriclaw.gallery.functional.downloader;

import okhttp3.OkHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HttpUrlDownloader implements ProgressCallback {

    private final String url;
    private final File saveFile;

    public HttpUrlDownloader(String url, File saveFile) {
        this.url = url;
        this.saveFile = saveFile;
    }

    public String getUrl() {
        return url;
    }

    public File getSavedFile() {
        return saveFile;
    }

    public boolean download(OkHttpClient client) {
        boolean isSuccess = false;
        try (BinaryFileDownloader downloader = new BinaryFileDownloader(client,
                new BinaryFileWriter(new FileOutputStream(this.saveFile), this))) {
            isSuccess = downloader.download(url);
            // TODO: download success
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!isSuccess) {
                this.saveFile.delete();
            }
        }
        return isSuccess;
    }

    @Override
    public void onProgress(long current, long total) {
        // TODO: Add progress update
    }

}
