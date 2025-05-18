package com.example.demo.repository;

import com.example.demo.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);

    Optional<ShortUrl> findByLongUrl(String shortCode);

    void deleteByCreatedAtBefore(Date expiryTime);
}
