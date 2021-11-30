package me.lyriclaw.gallery.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@Configuration
@ConfigurationProperties(prefix = "me.lyriclaw.gallery.storage")
@Data
public class StorageConfig {

    private Path resourcePath;
    private Path previewPath;

    @PostConstruct
    void mkdirPaths() {
        Path[] dirs = {resourcePath, previewPath};
        for (Path path : dirs) {
            if (!path.toFile().exists()) {
                //noinspection ResultOfMethodCallIgnored
                path.toFile().mkdirs();
            }
        }
    }

}
