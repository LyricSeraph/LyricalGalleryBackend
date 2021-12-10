package me.lyriclaw.gallery.functional;

import me.lyriclaw.gallery.functional.downloader.HttpUrlDownloader;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@SpringBootTest
public class HttpUrlDownloaderTest {

    @Autowired
    ApplicationContext context;

    @Test
    void downloadSuccessTest() throws IOException {
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
        HttpUrlDownloader downloader = new HttpUrlDownloader("http://httpbin.org/get", tempFile);
        boolean result = downloader.download(context.getBean(OkHttpClient.class));
        Assertions.assertTrue(result);
        Assertions.assertTrue(tempFile.length() > 0);
    }


    @Test
    void downloadFailedTest() throws IOException {
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), null);
        HttpUrlDownloader downloader = new HttpUrlDownloader("http://localhost:8888", tempFile);
        boolean result = downloader.download(context.getBean(OkHttpClient.class));
        Assertions.assertFalse(result);
        Assertions.assertFalse(tempFile.length() > 0);
    }

}
