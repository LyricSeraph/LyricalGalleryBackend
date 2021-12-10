package me.lyriclaw.gallery.functional.downloader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Objects;

import static com.google.common.net.HttpHeaders.CONTENT_LENGTH;

public class BinaryFileDownloader implements AutoCloseable {

    private final OkHttpClient client;
    private final BinaryFileWriter writer;

    public BinaryFileDownloader(OkHttpClient client, BinaryFileWriter writer) {
        this.client = client;
        this.writer = writer;
    }

    public boolean download(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.code() < 200 || response.code() >= 300) {
            return false;
        }
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return false;
        }
        long length = Long.parseLong(Objects.requireNonNull(response.header(CONTENT_LENGTH, "1")));
        writer.write(responseBody.byteStream(), length);
        return true;
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}