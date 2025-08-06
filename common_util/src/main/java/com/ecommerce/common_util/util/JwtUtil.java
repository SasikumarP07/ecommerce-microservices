package com.ecommerce.common_util.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class for handling JWT operations such as token generation,
 * validation, and extracting claims like user ID and role.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Generates the signing key from the secret.
     *
     * @return Key used for signing and validating the JWT.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token using the user ID and role.
     *
     * @param id   User ID to set as subject.
     * @param role User role to set as claim.
     * @return Signed JWT token.
     */
    public String generateToken(Long id, String role) {
        logger.debug("Generating token for user ID: {}, role: {}", id, role);
        long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the JWT token for authenticity and expiration.
     *
     * @param token JWT token to validate.
     * @return true if valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            logger.debug("Token validated successfully");
            return true;
        } catch (Exception e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extracts the subject (user ID) from the token.
     *
     * @param token JWT token.
     * @return User ID as a string.
     */
    public String extractUsername(String token) {
        String subject = extractAllClaims(token).getSubject();
        logger.debug("Extracted subject (user ID): {}", subject);
        return subject;
    }

    /**
     * Extracts the user role from the token.
     *
     * @param token JWT token.
     * @return Role string (e.g., "ROLE_USER", "ROLE_ADMIN").
     */
    public String extractUserRole(String token) {
        String role = extractAllClaims(token).get("role", String.class);
        logger.debug("Extracted role: {}", role);
        return role;
    }

    /**
     * Extracts a specific claim from the token using a claim resolver function.
     *
     * @param token          JWT token.
     * @param claimsResolver Function to resolve the desired claim.
     * @param <T>            Type of the claim to extract.
     * @return Extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the token.
     *
     * @param token JWT token.
     * @return Claims object containing all JWT claims.
     */
    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.debug("Claims extracted: {}", claims);
            return claims;
        } catch (Exception e) {
            logger.error("Failed to extract claims from token: {}", e.getMessage());
            throw e;
        }
    }
}
