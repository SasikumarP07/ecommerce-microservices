package com.ecommerce.order_service.configuration;

import com.ecommerce.common_util.filter.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain for Order Service");

        return http
                .csrf(csrf -> {
                    csrf.disable();
                    log.debug("CSRF protection disabled (stateless API)");
                })
                .headers(headers -> {
                    headers.frameOptions(frame -> frame.disable());
                    log.debug("Frame options disabled (to allow H2 console)");
                })
                .cors(Customizer.withDefaults()) // Default CORS config
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/public/**",
                            "/h2-console/**"
                    ).permitAll();
                    log.debug("Public access granted for H2 console and /public/** endpoints");

                    auth.anyRequest().authenticated();
                    log.debug("Authentication required for all other endpoints");
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("Creating AuthenticationManager bean");
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configuring CORS settings...");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // In production, use specific domains
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        log.debug("CORS allowed origins: *");
        log.debug("CORS allowed methods: GET, POST, PUT, DELETE, OPTIONS");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        log.info("CORS configuration completed");
        return source;
    }
}
