package com.ecommerce.notification_service.configuration;

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
import org.springframework.web.cors.*;

import java.util.List;

/**
 * 🔐 Security configuration class for the Notification Service.
 * Sets up JWT-based authentication, disables CSRF, configures CORS,
 * and allows specific public endpoints.
 */
@Configuration
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
@Slf4j
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // Custom JWT filter for token validation

    /**
     * Configures the Spring Security filter chain.
     * - Disables CSRF and frameOptions (for H2 console).
     * - Enables CORS.
     * - Allows public endpoints without authentication.
     * - Secures all other endpoints with JWT filter.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("🔐 Configuring SecurityFilterChain for Notification Service");

        return http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (for stateless APIs)
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Allow H2 console in iframe
                .cors(Customizer.withDefaults()) // Enable CORS using custom configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/public/**",
                                "/h2-console/**",
                                "/actuator/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll() // Public and Swagger access
                        .requestMatchers("/api/notifications/**").hasRole("USER") // Protected access
                        .anyRequest().authenticated() // Any other requests need authentication
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT filter
                .build();
    }


    /**
     * Exposes the AuthenticationManager bean for use in authentication flows.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("🔐 Initializing AuthenticationManager");
        return config.getAuthenticationManager();
    }

    /**
     * Provides the PasswordEncoder bean using BCrypt hashing algorithm.
     * This is not used in Notification Service but may be required for future enhancements.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("🔐 Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings to allow cross-origin requests.
     * - Allows all origins, methods, and headers.
     * - Allows credentials (cookies, tokens).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("🌐 Configuring CORS settings");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // Allow all origins (not recommended for production)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // HTTP methods allowed
        config.setAllowedHeaders(List.of("*")); // Allow all headers
        config.setAllowCredentials(true); // Allow cookies/token headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply CORS config to all endpoints
        return source;
    }
}
