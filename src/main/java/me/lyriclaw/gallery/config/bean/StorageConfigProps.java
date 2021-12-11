package me.lyriclaw.gallery.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.file.Path;

@Configuration
@ConfigurationProperties(prefix = "project.storage")
@Data
public class StorageConfigProps {

    private Path resourcePath;
    private Path thumbnailPath;

    @PostConstruct
    void mkdirPaths() {
        Path[] dirs = {resourcePath, thumbnailPath};
        for (Path path : dirs) {
            if (!path.toFile().exists()) {
                //noinspection ResultOfMethodCallIgnored
                path.toFile().mkdirs();
            }
        }
    }

}
