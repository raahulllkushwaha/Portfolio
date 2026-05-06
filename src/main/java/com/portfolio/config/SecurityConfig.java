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
                // 1. MUST DISABLE CSRF for stateless REST APIs
                .csrf(AbstractHttpConfigurer::disable)

                // 2. ENABLE CORS using the bean defined below
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Set session management to STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Request Authorization
                .authorizeHttpRequests(auth -> auth
                        // Always allow OPTIONS for CORS pre-flight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Public Auth and Contact endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/contact/**").permitAll()

                        // Public GET endpoints using double asterisks for path robustness
                        .requestMatchers(HttpMethod.GET, "/api/profile/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/skills/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/experiences/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/education/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/certifications/**").permitAll()

                        // Static resources & H2 Console (if needed)


                        // All other requests (POST/PUT/DELETE on actual data) require ADMIN
                        .anyRequest().hasRole("ADMIN")
                )

                // 5. Fix for Browser Frame Options
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

                // 6. Add JWT Filter before the standard Username/Password filter
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