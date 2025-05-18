package com.example.demo.service;

import com.example.demo.repository.ShortUrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class ShortUrlCleanerService {
    @Autowired private ShortUrlRepository shortUrlRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpiredUrls() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        Date threeDaysAgo = calendar.getTime();

        shortUrlRepository.deleteByCreatedAtBefore(threeDaysAgo);
        log.info("✅ 已清除過期短網址");
    }
}
