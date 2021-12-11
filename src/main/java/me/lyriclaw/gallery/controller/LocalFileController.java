package me.lyriclaw.gallery.controller;

import eu.medsea.mimeutil.MimeUtil;
import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.config.bean.StorageConfigProps;
import me.lyriclaw.gallery.constants.LocalFileResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

@Controller
@Slf4j
public class LocalFileController {

    private final StorageConfigProps storageConfigProps;
    private final ResourceLoader resourceLoader;

    @Autowired
    public LocalFileController(StorageConfigProps storageConfigProps, ResourceLoader resourceLoader) {
        this.storageConfigProps = storageConfigProps;
        this.resourceLoader = resourceLoader;
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }

    private Resource loadResource(String filePath) {
        return resourceLoader.getResource("file://" + filePath);
    }

    @GetMapping("/{resource_type}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable("resource_type") LocalFileResourceType type, @PathVariable("filename") String filename) {
        if (filename.contains("..")) {
            return ResponseEntity.badRequest().build();
        }
        Path baseDirectory = null;
        switch (type) {
            case resources:
                baseDirectory = storageConfigProps.getResourcePath();
                break;
            case thumbnails:
                baseDirectory = storageConfigProps.getThumbnailPath();
                break;
        }
        Path path = Paths.get(baseDirectory.toString(), filename);
        Resource file = loadResource(path.toString());
        if (file.exists()) {
            Collection<?> mimeTypes = MimeUtil.getMimeTypes(path.toFile());
            String mimeType = MimeUtil.getFirstMimeType(mimeTypes.toString()).toString();
            return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(file);
        }
        return ResponseEntity.notFound().build();
    }

}
