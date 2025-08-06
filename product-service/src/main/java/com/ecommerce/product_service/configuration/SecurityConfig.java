package com.ecommerce.product_service.configuration;

import com.ecommerce.common_util.filter.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 🔐 SecurityConfig sets up security configurations for the Product Service.
 * It uses a stateless session policy, applies role-based access control, and integrates JWT-based authentication.
 */
@Configuration
@Slf4j
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Constructor-based injection for JwtFilter from the common_util module.
     *
     * @param jwtFilter the custom JWT filter used to validate incoming tokens.
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures HTTP security settings for the application.
     * <ul>
     *     <li>Disables CSRF (suitable for stateless REST APIs).</li>
     *     <li>Sets session management to stateless.</li>
     *     <li>Defines public endpoints for product browsing and category viewing.</li>
     *     <li>Restricts product and category management endpoints to users with ADMIN role.</li>
     *     <li>Requires authentication for all other endpoints.</li>
     *     <li>Adds a JWT filter before the default Spring Security filter.</li>
     * </ul>
     *
     * @param http the HttpSecurity object
     * @return SecurityFilterChain bean
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("🔒 Configuring security filter chain...");

        http
                .csrf(csrf -> {
                    log.debug("Disabling CSRF protection");
                    csrf.disable();
                })
                .sessionManagement(session -> {
                    log.debug("Setting session policy to STATELESS");
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(auth -> {
                    log.debug("🔓 Permitting Swagger UI and API docs access");
                    auth
                            .requestMatchers(
                                    "/v3/api-docs/**",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/webjars/**"
                            ).permitAll();

                    log.debug("🌐 Permitting public access to product and category listings");
                    auth
                            .requestMatchers(
                                    "/products/latest", "/products/suggested/**", "/products",
                                    "/products/{id}", "/products/filter",
                                    "/categories", "/categories/{id}"
                            ).permitAll();

                    log.debug("🔐 Restricting ADMIN-only access to product and category management");
                    auth
                            .requestMatchers("/products/**").hasRole("ADMIN")
                            .requestMatchers("/categories/**").hasRole("ADMIN");

                    log.debug("🔐 All other endpoints require authentication");
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("✅ Security filter chain configured");
        return http.build();
    }


    /**
     * Provides the AuthenticationManager bean, required for Spring Security authentication.
     *
     * @param config the AuthenticationConfiguration object
     * @return the AuthenticationManager
     * @throws Exception in case of errors retrieving the manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("🔑 Creating AuthenticationManager bean");
        return config.getAuthenticationManager();
    }
}
