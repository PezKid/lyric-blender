package com.spotifylyrics.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotifylyrics.service.OpenAIService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/lyrics")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class LyricsController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/generate")
    public Mono<Map> generateLyrics(@RequestBody Map<String, String> request) {
        String artist = request.get("artist");
        String genre = request.get("genre");
        String theme = request.get("theme");
        
        return openAIService.generateLyrics(artist, genre, theme);
    }

    @PostMapping("/blend")
    public Mono<Map> generateBlendedLyrics(@RequestBody Map<String, String> request) {
        String artist1 = request.get("artist1");
        String artist2 = request.get("artist2");
        String genre = request.get("genre");
        String theme = request.get("theme");
        
        return openAIService.generateBlendedLyrics(artist1, artist2, genre, theme);
    }

    @GetMapping("/test")
    public Mono<Map> testGeneration() {
        return openAIService.generateLyrics("The 1975", "pop", "dating apps");
    }
}