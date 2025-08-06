package com.ecommerce.inventory_service.configuration;

import com.ecommerce.common_util.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 🔐 Security configuration class for the Inventory Service.
 * <p>
 * This class defines security rules using Spring Security, such as:
 * - Configuring JWT-based authentication
 * - Defining endpoint access rules
 * - Enabling CORS
 * - Disabling CSRF and frame options (for H2 console)
 * </p>
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Configures the Spring Security filter chain.
     * - Disables CSRF
     * - Enables CORS
     * - Allows unauthenticated access to `/public/**` and `/h2-console/**`
     * - Secures all other endpoints with JWT authentication
     * - Registers the custom JwtFilter before UsernamePasswordAuthenticationFilter
     *
     * @param http HttpSecurity object provided by Spring Security
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("🔐 Configuring security filter chain for Inventory Service...");

        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // ✅ Allow access to Swagger UI and H2 console
                        .requestMatchers(
                                "/h2-console/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ✅ Secure inventory endpoints (role-based protection)
                        .requestMatchers("/api/inventory/**").hasRole("ADMIN")

                        // ✅ Any other endpoints must be authenticated
                        .anyRequest().authenticated()
                )
                // ✅ Add JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provides a BCryptPasswordEncoder bean used for encoding and verifying passwords.
     *
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("🔑 Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager bean from the AuthenticationConfiguration.
     * Useful for authenticating users manually in custom auth flows.
     *
     * @param config AuthenticationConfiguration provided by Spring
     * @return AuthenticationManager instance
     * @throws Exception if authentication manager can't be built
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("⚙️ Creating AuthenticationManager bean");
        return config.getAuthenticationManager();
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) policy.
     * Allows all origins and standard HTTP methods.
     * Note: In production, you should restrict origins for better security.
     *
     * @return CorsConfigurationSource to be used by Spring Security
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("🌐 Configuring CORS");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // In production, restrict this
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
