package com.spotifylyrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class SpotifyLyricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyLyricsApplication.class, args);
    }

    @GetMapping("/api/health")
    public String health() {
        return "Spotify Lyrics Generator Backend is running!";
    }
    
    @GetMapping("/api/test")
    public java.util.Map<String, Object> test() {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Backend is working");
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("status", "success");
        return response;
    }
}