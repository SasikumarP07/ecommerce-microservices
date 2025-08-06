package com.ecommerce.user_service.configuration;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuration class that sets up a Feign client interceptor to propagate the
 * Authorization token from the incoming HTTP request to any outgoing Feign requests.
 * <p>
 * This is useful for preserving security context (such as JWT tokens) when making
 * inter-service calls in a microservices architecture.
 */
@Configuration
@Slf4j
public class FeignClientInterceptorConfig {

    /**
     * Defines a {@link RequestInterceptor} bean for Feign clients.
     * <p>
     * This interceptor retrieves the Authorization token from the current HTTP request
     * and forwards it in the headers of all outgoing Feign requests.
     *
     * @return the configured {@link RequestInterceptor}
     */
    @Bean
    public RequestInterceptor feignClientInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String token = request.getHeader("Authorization");
                if (token != null) {
                    log.debug("🛡️ Forwarding Authorization token in Feign request");
                    requestTemplate.header("Authorization", token);
                } else {
                    log.warn("⚠️ No Authorization token found in incoming request");
                }
            } else {
                log.warn("⚠️ No ServletRequestAttributes available. Feign request might be outside HTTP context.");
            }
        };
    }
}
