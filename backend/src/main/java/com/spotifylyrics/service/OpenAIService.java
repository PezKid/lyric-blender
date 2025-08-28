package com.spotifylyrics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class OpenAIService {

    private final WebClient webClient;

    @Value("${openai.api-key}")
    private String apiKey;

    public OpenAIService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .build();
    }

    public Mono<Map> generateLyrics(String artist, String genre, String theme) {
        Map<String, Object> request = createLyricsRequest(artist, genre, theme);

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class);
    }

    private Map<String, Object> createLyricsRequest(String artist, String genre, String theme) {
        Map<String, Object> request = new HashMap<>();
        
        request.put("model", "gpt-4o-mini");
        request.put("max_tokens", 500);  // Enough for full song
        request.put("temperature", 0.8); // Creative but not too random
        request.put("top_p", 0.9);       // Good diversity

        String prompt = buildLyricsPrompt(artist, genre, theme);
        
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", 
            "You are a creative songwriter. Generate original lyrics that capture the style and themes of specific artists without copying existing songs. Focus on matching their typical song structure, vocabulary, and emotional tone."));
        messages.add(Map.of("role", "user", "content", prompt));
        
        request.put("messages", messages);
        
        return request;
    }

    private String buildLyricsPrompt(String artist, String genre, String theme) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Generate original song lyrics");
        
        if (artist != null && !artist.isEmpty()) {
            prompt.append(" in the style of ").append(artist);
        }
        
        if (genre != null && !genre.isEmpty()) {
            prompt.append(" with ").append(genre).append(" influences");
        }
        
        if (theme != null && !theme.isEmpty()) {
            prompt.append(" about ").append(theme);
        }
        
        prompt.append(". ");
        prompt.append("Include a verse and chorus. ");
        prompt.append("Match the typical song structure, vocabulary, and emotional tone of this style. ");
        prompt.append("Make it original and creative, not a copy of existing songs.");
        
        return prompt.toString();
    }

    public Mono<Map> generateBlendedLyrics(String artist1, String artist2, String genre, String theme) {
        String blendPrompt = String.format(
            "Create an original song that is a blend of the lyrical styles of %s and %s." +
            "Do NOT do a basic rhyme scheme of A A B B unless that artist is known for it." +
            "Actually look at the lyrical rhyme schemes and structures of the songs of both artists and blend them." +
            "The song should be of the %s genre and written about %s with the writing styles of both artists.",
            artist1, artist2, genre, theme
        );

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-4o-mini");
        request.put("max_tokens", 600);  // Bit longer for blended complexity
        request.put("temperature", 0.85); // Slightly more creative for blending
        request.put("top_p", 0.9);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", 
            "You are an expert at analyzing and blending musical styles. Create original lyrics that authentically combine different artists' approaches without copying existing songs."));
        messages.add(Map.of("role", "user", "content", blendPrompt));

        request.put("messages", messages);

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class);
    }
}