package com.ecommerce.auth_service.configuration;

import com.ecommerce.auth_service.filter.JwtFilter;
import com.ecommerce.auth_service.serviceImplementation.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 🔐 SecurityConfig configures Spring Security settings for the Auth Service.
 * It handles JWT-based stateless authentication, CORS setup, endpoint access rules, and essential beans.
 */
@Configuration
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
@Slf4j
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Defines the Spring Security filter chain configuration.
     * - Disables CSRF for REST API.
     * - Enables CORS for cross-origin requests.
     * - Sets session management to stateless.
     * - Allows unauthenticated access to /auth/login and /auth/signup.
     * - Secures all other endpoints.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("🔒 Configuring SecurityFilterChain...");

        http
                .csrf(csrf -> {
                    csrf.disable();
                    log.debug("✅ CSRF protection disabled");
                })
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                    log.debug("🌐 CORS configuration applied");
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    log.debug("📦 Session management set to STATELESS");
                })
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(
                                    "/auth/login",
                                    "/auth/signup",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/v3/api-docs",
                                    "/webjars/**",
                                    "/swagger-resources/**"
                            ).permitAll(); // Allow Swagger and auth endpoints

                    auth.anyRequest().authenticated(); // All other endpoints require authentication

                    log.debug("🔐 Public endpoints set for Swagger and /auth/login, /auth/signup. Others secured.");
                });

        // Add JWT filter before the default UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        log.debug("🛡️ JWT filter added before UsernamePasswordAuthenticationFilter");

        return http.build();
    }


    /**
     * Configures global CORS policy.
     * Allows all origins, headers, and common HTTP methods.
     * ⚠️ In production, restrict origins to trusted domains only.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("🌐 Creating CORS configuration source...");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // Replace "*" with specific domains in production
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // Allows cookies and headers to be sent

        log.debug("✅ CORS allowed origins: {}, methods: {}, headers: {}",
                config.getAllowedOrigins(), config.getAllowedMethods(), config.getAllowedHeaders());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        log.info("🌍 CORS configuration registered for all endpoints");

        return source;
    }

    /**
     * Creates an AuthenticationManager bean using Spring's AuthenticationConfiguration.
     * Required for manual authentication in login endpoints.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("🔐 Creating AuthenticationManager bean...");
        return config.getAuthenticationManager();
    }

    /**
     * Password encoder bean using BCrypt algorithm.
     * Used for hashing user passwords before storing in the database.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("🔑 Creating BCryptPasswordEncoder bean...");
        return new BCryptPasswordEncoder();
    }

    /**
     * LoadBalanced RestTemplate bean for inter-service communication using service names (Eureka discovery).
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        log.info("🔁 Creating LoadBalanced RestTemplate bean...");
        return new RestTemplate();
    }
}
