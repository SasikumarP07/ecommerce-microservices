package com.ecommerce.common_util.filter;

import com.ecommerce.common_util.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JwtFilter is a custom Spring Security filter that validates JWT tokens on each request.
 * <p>
 * This filter checks the Authorization header for a Bearer token, validates the token,
 * extracts the username and role, and sets the authentication in the SecurityContextHolder.
 * <p>
 * It runs once per request using Spring's {@link OncePerRequestFilter}.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Intercepts each HTTP request to validate the JWT token from the Authorization header.
     *
     * @param request     the incoming HTTP request
     * @param response    the outgoing HTTP response
     * @param filterChain the filter chain to pass the request and response to the next filter
     * @throws ServletException if an error occurs during filtering
     * @throws IOException      if an input or output exception occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.debug("JWT token received: {}", token);

            try {
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractUserRole(token);

                    logger.info("Valid JWT token. User: {}, Role: {}", email, role);

                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("Security context updated for user: {}", email);
                } else {
                    logger.warn("Invalid JWT token received");
                }
            } catch (Exception e) {
                logger.error("Exception during JWT processing: {}", e.getMessage());
            }

        } else {
            logger.debug("No Authorization header or token does not start with Bearer");
        }

        filterChain.doFilter(request, response);
    }
}
