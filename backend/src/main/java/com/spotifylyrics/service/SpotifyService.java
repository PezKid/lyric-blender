package com.spotifylyrics.service;

import java.util.Map;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class SpotifyService {

    private final WebClient webClient;

    public SpotifyService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.spotify.com/v1")
                .build();
    }

    public Mono<Map> getUserProfile(OAuth2AuthorizedClient authorizedClient) {
        return webClient.get()
                .uri("/me")
                .headers(headers -> headers.setBearerAuth(
                    authorizedClient.getAccessToken().getTokenValue()))
                .retrieve()
                .bodyToMono(Map.class);
    }

    public Mono<Map> getTopArtists(OAuth2AuthorizedClient authorizedClient) {
        return webClient.get()
                .uri("/me/top/artists?limit=20&time_range=medium_term")
                .headers(headers -> headers.setBearerAuth(
                    authorizedClient.getAccessToken().getTokenValue()))
                .retrieve()
                .bodyToMono(Map.class);
    }

    public Mono<Map> getRecentlyPlayed(OAuth2AuthorizedClient authorizedClient) {
        return webClient.get()
                .uri("/me/player/recently-played?limit=20")
                .headers(headers -> headers.setBearerAuth(
                    authorizedClient.getAccessToken().getTokenValue()))
                .retrieve()
                .bodyToMono(Map.class);
    }
}