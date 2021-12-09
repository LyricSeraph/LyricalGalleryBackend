package me.lyriclaw.gallery.constants;

public enum PreviewSize {

    small(128),
    medium(256),
    large(384),
    ;

    private int size;

    PreviewSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
