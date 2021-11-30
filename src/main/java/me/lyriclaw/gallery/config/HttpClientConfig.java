package me.lyriclaw.gallery.config;

import lombok.extern.slf4j.Slf4j;
import me.lyriclaw.gallery.functional.downloader.HttpUrlDownloader;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.*;

@Configuration
@Slf4j
public class HttpClientConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Proxy createProxy(@Value("${me.lyriclaw.gallery.download.proxy}") String proxyString) {
        if (!StringUtils.hasLength(proxyString)) {
            return Proxy.NO_PROXY;
        }

        try {
            URI uri = URI.create(proxyString);
            SocketAddress sa = new InetSocketAddress(uri.getHost() + uri.getPath(), uri.getPort());
            if (uri.getScheme().startsWith("http") || uri.getScheme().startsWith("https")) {
                return new Proxy(Proxy.Type.HTTP, sa);
            } else if (uri.getScheme().startsWith("socks")) {
                return new Proxy(Proxy.Type.SOCKS, sa);
            } else {
                log.warn("Unsupported proxy protocol: " + uri.getScheme());
            }
        } catch (Exception ex) {
            log.warn("Cannot recognize proxy url: " + proxyString);
        }
        return Proxy.NO_PROXY;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public OkHttpClient createHttpClient(@Autowired Proxy proxy) {
        return new OkHttpClient.Builder()
                .proxy(proxy)
                .build();
    }

}
