package com.example.demo.service;

import com.example.demo.entity.ShortUrl;
import com.example.demo.repository.ShortUrlRepository;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
public class UrlShortenerService {
    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;

    public UrlShortenerService(
            ShortUrlRepository shortUrlRepository, UserRepository userRepository) {
        this.shortUrlRepository = shortUrlRepository;
        this.userRepository = userRepository;
    }

    // @Transactional
    public String shortenUrl(String longUrl) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        log.info("Authenticated user: " + auth.getName());
        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        String shortCode;
        boolean isSaved = false;
        do {
            shortCode = "http://sho.rt/" + this.generateRandomCode(8);

            if (shortUrlRepository.findByShortCode(shortCode).isEmpty()) {
                try {
                    ShortUrl shortUrl =
                            ShortUrl.builder()
                                    .longUrl(longUrl)
                                    .shortCode(shortCode)
                                    .user(user)
                                    .build();
                    shortUrlRepository.save(shortUrl);
                    isSaved = true;
                } catch (DataIntegrityViolationException e) {
                    log.warn(
                            "Duplicate shortCode detected during save: {}. Retrying...", shortCode);
                }
            }

        } while (!isSaved);
        return shortCode;
    }

    private String generateRandomCode(final int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(chars.charAt(random.nextInt(chars.length())));
        }
        return stringBuilder.toString();
    }
}
