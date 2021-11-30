package me.lyriclaw.gallery.service;

import me.lyriclaw.gallery.constants.PreviewSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface StorageService {

    boolean store(MultipartFile file, String filename);

    boolean store(File localFile, String filename);

    void delete(String filename);

    String getUrl(String filename);

}