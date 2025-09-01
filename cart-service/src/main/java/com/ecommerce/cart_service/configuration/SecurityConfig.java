package com.ecommerce.cart_service.configuration;

import com.ecommerce.common_util.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Cart Service.
 * This class configures Spring Security to:
 * - Disable CSRF
 * - Use stateless session management
 * - Protect cart-related endpoints
 * - Add JWT filter for token-based authentication
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Configures the security filter chain.
     * - Disables CSRF protection
     * - Sets session management to stateless
     * - Allows unrestricted access to `/actuator/**`
     * - Secures `/api/carts/**` to users with `ROLE_USER`
     * - Secures all other endpoints with authentication
     * - Adds custom JWT filter before UsernamePasswordAuthenticationFilter
     *
     * @param http HttpSecurity object to customize security settings
     * @return configured SecurityFilterChain
     * @throws Exception in case of configuration failure
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain for Cart Service...");

        http
                .csrf(csrf -> {
                    csrf.disable();
                    log.debug("CSRF protection disabled");
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    log.debug("Session management set to STATELESS");
                })
                .authorizeHttpRequests(auth -> {
                    // Public endpoints
                    auth.requestMatchers("/actuator/**").permitAll();
                    log.info("Actuator endpoints are public");

                    // Protected endpoints
                    auth.requestMatchers("/api/carts/**").hasRole("USER");
                    log.info("Protected access required for /api/carts/**");

                    // Any other request requires authentication
                    auth.anyRequest().authenticated();
                    log.debug("Any other request requires authentication");
                })
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("JWT Filter added to security chain");

        return http.build();
    }




    /**
     * Exposes the AuthenticationManager bean.
     * This bean is required by Spring Security to perform authentication.
     *
     * @param config the AuthenticationConfiguration
     * @return AuthenticationManager instance
     * @throws Exception if the configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        log.info("Creating AuthenticationManager bean...");
        return config.getAuthenticationManager();
    }
}
