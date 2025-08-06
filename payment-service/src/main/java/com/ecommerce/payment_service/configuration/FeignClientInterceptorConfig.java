package com.ecommerce.payment_service.configuration;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuration class for Feign client interceptors.
 * <p>
 * This interceptor is responsible for forwarding the `Authorization` header
 * from the incoming HTTP request to all outgoing Feign client requests.
 * This ensures JWT token propagation across microservices.
 * </p>
 */
@Slf4j
@Configuration
public class FeignClientInterceptorConfig {

    /**
     * Defines a Feign {@link RequestInterceptor} bean that intercepts outgoing
     * Feign requests and appends the `Authorization` header from the current
     * HTTP servlet request.
     *
     * @return the configured {@link RequestInterceptor}
     */
    @Bean
    public RequestInterceptor feignClientInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader("Authorization");
                if (token != null) {
                    log.debug("Forwarding Authorization token in Feign request");
                    requestTemplate.header("Authorization", token);
                } else {
                    log.warn("No Authorization token found in current request");
                }
            } else {
                log.warn("No ServletRequestAttributes found - could not set Authorization header for Feign request");
            }
        };
    }
}
