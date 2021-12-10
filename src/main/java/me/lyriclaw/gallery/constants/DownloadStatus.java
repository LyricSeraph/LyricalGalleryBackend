package me.lyriclaw.gallery.constants;

public enum DownloadStatus {

    IDLE(0),
    DOWNLOADING(1),
    FINISHED(2),
    FAILED(3),
    ;

    private int statusCode;

    DownloadStatus(int code) {
        this.statusCode = code;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
