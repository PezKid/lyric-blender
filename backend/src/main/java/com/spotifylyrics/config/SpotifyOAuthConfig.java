package com.spotifylyrics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SpotifyOAuthConfig {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(spotifyClientRegistration());
    }

    private ClientRegistration spotifyClientRegistration() {
      return ClientRegistration.withRegistrationId("spotify")
              .clientId(clientId)
              .clientSecret(clientSecret)
              .redirectUri(this.redirectUri)
              .authorizationUri("https://accounts.spotify.com/authorize")
              .tokenUri("https://accounts.spotify.com/api/token")
              .userInfoUri("https://api.spotify.com/v1/me")
              .userNameAttributeName("id")
              .scope("user-read-private", "user-read-email", "user-top-read", "user-read-recently-played")
              .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
              .clientName("Spotify")
              .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/health", "/api/test", "/login/**", "/oauth2/**", "/api/spotify/debug-config").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("http://127.0.0.1:3000/", true)
                .failureUrl("http://127.0.0.1:3000/error")
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );

        return http.build();
    }
}