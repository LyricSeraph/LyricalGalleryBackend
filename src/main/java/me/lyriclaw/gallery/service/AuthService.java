package me.lyriclaw.gallery.service;

import me.lyriclaw.gallery.config.bean.AuthConfigProps;
import me.lyriclaw.gallery.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class AuthService {

    private static final String AUTH_SALT = "LyricalGallery";

    private final AuthConfigProps authConfigProps;

    private Set<String> tokenSet;

    @Autowired
    public AuthService(AuthConfigProps authConfigProps) {
        this.authConfigProps = authConfigProps;
        tokenSet = new ConcurrentSkipListSet<>();
    }

    public boolean isAuthenticated(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String tokenString = httpServletRequest.getHeader("TOKEN");
        return !StringUtils.hasLength(authConfigProps.getAuthKey())
                || (StringUtils.hasLength(tokenString) && tokenSet.contains(tokenString));
    }

    @Scheduled(cron = "0 * * * * *")
    void refreshToken() {
        Instant time = Instant.now();
        int currentMinute = (int) (time.getEpochSecond() / 60);
        Set<String> newTokenSet = new ConcurrentSkipListSet<>();
        for (int i = -2; i <= 2; i++) {
            try {
                String minutePart = Md5Utils.md5(String.valueOf(currentMinute + i));
                String keyPart = Md5Utils.md5(authConfigProps.getAuthKey() + AUTH_SALT);
                String token = Md5Utils.md5(minutePart + keyPart);
                newTokenSet.add(token);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        tokenSet = newTokenSet;
    }

}
