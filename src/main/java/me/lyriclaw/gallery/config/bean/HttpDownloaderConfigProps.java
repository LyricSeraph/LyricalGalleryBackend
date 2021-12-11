package me.lyriclaw.gallery.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "project.downloader")
@Data
public class HttpDownloaderConfigProps {

    private String proxy;

}
