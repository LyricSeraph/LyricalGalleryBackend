package me.lyriclaw.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LyricalGalleryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LyricalGalleryBackendApplication.class, args);
    }

}
