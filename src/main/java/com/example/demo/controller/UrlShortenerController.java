package com.example.demo.controller;

import com.example.demo.service.UrlShortenerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlShortenerController {
    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("health-check/")
    public String checkHealth() {
        return "ok";
    }

    @PostMapping("shorten/")
    public String shortenUrl(@RequestParam String longUrl) {
        return this.urlShortenerService.shortenUrl(longUrl);
    }
}
