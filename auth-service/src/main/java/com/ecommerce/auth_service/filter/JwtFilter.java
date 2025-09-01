package com.ecommerce.auth_service.filter;

import com.ecommerce.auth_service.serviceImplementation.CustomUserDetailsService;
import com.ecommerce.common_util.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT filter for validating requests by extracting and verifying the JWT token.
 * This filter intercepts every request once and checks for a valid token in the Authorization header.
 * If the token is valid, it sets up Spring Security's authentication context.
 */
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Filters incoming requests to validate JWT tokens and set up the Spring Security context.
     *
     * @param request     The incoming HTTP request.
     * @param response    The outgoing HTTP response.
     * @param filterChain The filter chain to continue processing.
     * @throws ServletException If a servlet error occurs.
     * @throws IOException      If an input or output error is detected.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String requestPath = request.getServletPath();

        log.debug("Intercepted request to path: {}", requestPath);
        log.debug("Authorization Header: {}", authHeader);

        // Skip filtering for public endpoints
        if (requestPath.startsWith("/auth/signup") || requestPath.startsWith("/auth/login")) {
            log.info("Skipping JWT filter for public endpoint: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = null;
        String username = null;

        // Extract JWT token from the Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
                log.info("Extracted username from JWT: {}", username);
            } catch (Exception e) {
                log.error("Failed to extract username from JWT: {}", e.getMessage(), e);
            }
        }

        // Authenticate the user if JWT is valid and user is not yet authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwtToken)) {
                log.info("JWT token is valid for user: {}", username);

                // Extract role from token and set it in authorities
                String role = jwtUtil.extractClaim(jwtToken, claims -> claims.get("role", String.class));
                log.debug("Extracted role from JWT: {}", role);

                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                // Build the authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Security context updated with authenticated user: {}", username);
            } else {
                log.warn("JWT token is invalid for user: {}", username);
            }
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    /**
     * Determines whether this filter should be skipped for the given request.
     * Public endpoints like `/auth/login` and `/auth/signup` are not filtered.
     *
     * @param request The HTTP request to check.
     * @return true if filtering should be skipped, false otherwise.
     * @throws ServletException If an error occurs while evaluating the request.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        log.debug("shouldNotFilter evaluated for path: {}", path);
        return path.equals("/auth/login") || path.equals("/auth/signup");
    }
}
