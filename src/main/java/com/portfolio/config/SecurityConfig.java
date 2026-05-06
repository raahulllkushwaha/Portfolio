package com.portfolio.config;

import com.portfolio.security.AdminUserDetailsService;
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
import java.util.List;

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
                // 1. Explicitly disable CSRF for APIs
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Attach our custom CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. Stateless sessions (no cookies)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Permission Handshake
                .authorizeHttpRequests(auth -> auth
                        // Allow browser pre-flight OPTIONS requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth & Public POST endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()

                        // Public GET endpoints - using wildcards to cover /api/projects and /api/projects/
                        .requestMatchers(HttpMethod.GET, "/api/profile/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/skills/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/experiences/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/education/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/certifications/**").permitAll()

                        // Permit static resources and H2 (if applicable)
                        .requestMatchers("/h2-console/**").permitAll()

                        // Everything else (POST/PUT/DELETE on data) requires Admin role
                        .anyRequest().hasRole("ADMIN")
                )

                // 5. Security Header Fixes
                .headers(headers -> headers
                        // Disable X-Frame-Options to allow the app to work in all browsers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )

                // 6. JWT Filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Use setAllowedOriginPatterns for more flexible matching with credentials
        if (allowedOrigins != null && allowedOrigins.length > 0) {
            config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        } else {
            config.addAllowedOrigin("*");
        }

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}