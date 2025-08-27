package com.spotifylyrics.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotifylyrics.service.SpotifyService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyController {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    @Autowired
    private SpotifyService spotifyService;

    @GetMapping("/callback")
    public Mono<Map> handleCallback(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        return spotifyService.getUserProfile(authorizedClient);
    }

    @GetMapping("/profile")
    public Mono<Map> getUserProfile(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        return spotifyService.getUserProfile(authorizedClient);
    }

    @GetMapping("/top-artists")
    public Mono<Map> getTopArtists(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        return spotifyService.getTopArtists(authorizedClient);
    }

    @GetMapping("/recent")
    public Mono<Map> getRecentlyPlayed(
            @RegisteredOAuth2AuthorizedClient("spotify") OAuth2AuthorizedClient authorizedClient) {
        return spotifyService.getRecentlyPlayed(authorizedClient);
    }

    @GetMapping("/debug-config")
    public Map<String, String> debugConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("clientId_from_spring", clientId != null && !clientId.isEmpty() ? "SET" : "!!! NOT SET OR EMPTY !!!");
        config.put("clientSecret_from_spring", clientSecret != null && !clientSecret.isEmpty() ? "SET" : "!!! NOT SET OR EMPTY !!!");
        config.put("redirectUri_from_spring", redirectUri);
        return config;
    }
} 