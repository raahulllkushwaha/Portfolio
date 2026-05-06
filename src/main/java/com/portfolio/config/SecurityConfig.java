package com.portfolio.config;

import com.portfolio.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow EVERY Pre-flight (OPTIONS) request
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. Broad Matchers for Public Data
                        .requestMatchers("/api/auth/**", "/api/contact/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profile**", "/api/profile/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/projects**", "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/skills**", "/api/skills/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/experiences**", "/api/experiences/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/education**", "/api/education/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/certifications**", "/api/certifications/**").permitAll()

                        // 3. Fallback for any other path starting with /api/
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()

                        // 4. Secure everything else
                        .anyRequest().hasRole("ADMIN")
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Handle potential empty origins by falling back to a safe default
        if (allowedOrigins != null && allowedOrigins.length > 0) {
            config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        } else {
            // Only use "*" during extreme troubleshooting; production should be specific
            config.setAllowedOrigins(Collections.singletonList("*"));
        }

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        config.setExposedHeaders(Collections.singletonList("Authorization"));

        // Credentials must be true if your React app uses 'withCredentials' or sends Auth headers
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Register for all paths
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}