package me.lyriclaw.gallery.functional.downloader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BinaryFileWriter implements AutoCloseable {

    private final int CHUNK_SIZE = 65536;

    private final OutputStream outputStream;

    private final ProgressCallback progressCallback;

    public BinaryFileWriter(OutputStream outputStream, ProgressCallback progressCallback) {
        this.outputStream = outputStream;
        this.progressCallback = progressCallback;
    }

    public long write(InputStream inputStream, long length) throws IOException {
        try (BufferedInputStream input = new BufferedInputStream(inputStream)) {
            byte[] dataBuffer = new byte[CHUNK_SIZE];
            int readBytes;
            long totalBytes = 0;
            while ((readBytes = input.read(dataBuffer)) != -1) {
                totalBytes += readBytes;
                outputStream.write(dataBuffer, 0, readBytes);
                progressCallback.onProgress(totalBytes, length);
            }
            return totalBytes;
        }
    }

    @Override
    public void close() throws Exception {
        if (outputStream != null) {
            outputStream.close();
        }
    }
}

