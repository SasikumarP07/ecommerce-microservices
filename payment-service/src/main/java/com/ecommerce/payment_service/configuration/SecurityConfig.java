package com.ecommerce.payment_service.configuration;

import com.ecommerce.common_util.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Payment Service.
 * <p>
 * Configures HTTP security, JWT filter integration, session policy,
 * password encoding, and authentication manager.
 * </p>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Configures the security filter chain for the application.
     * <p>
     * - Disables CSRF protection (as it is a stateless service)
     * - Secures `/payments/**` endpoints with JWT authentication
     * - Allows all other requests without authentication
     * - Sets session creation policy to stateless
     * - Adds a custom JWT filter before Spring's authentication filter
     * </p>
     *
     * @param http the {@link HttpSecurity} object to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if any configuration error occurs
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("💳 Configuring SecurityFilterChain for Payment Service...");

        SecurityFilterChain chain = http
                .csrf(csrf -> {
                    csrf.disable();
                    log.debug("✅ CSRF disabled");
                })
                .headers(headers -> {
                    headers.frameOptions(frame -> frame.disable());
                    log.debug("✅ Frame options disabled (for H2 console)");
                })
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(
                                    "/payments/**"
                            ).authenticated();
                    log.debug("🔒 Secured endpoint: /payments/** (requires authentication)");

                    auth
                            .requestMatchers(
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/actuator/**"
                            ).permitAll();
                    log.debug("📘 Permitted Swagger and actuator endpoints");

                    auth
                            .anyRequest().permitAll();
                    log.debug("🌐 All other endpoints permitted");
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    log.debug("📦 Session management set to STATELESS");
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

        log.info("✅ SecurityFilterChain configured successfully for Payment Service");
        return chain;
    }



    /**
     * Provides the authentication manager bean required by Spring Security.
     *
     * @param config the authentication configuration
     * @return the {@link AuthenticationManager} instance
     * @throws Exception if any error occurs during retrieval
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("🔐 Initializing AuthenticationManager");
        return config.getAuthenticationManager();
    }

    /**
     * Provides a password encoder bean using BCrypt hashing algorithm.
     *
     * @return the {@link PasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("🔑 Using BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }
}
