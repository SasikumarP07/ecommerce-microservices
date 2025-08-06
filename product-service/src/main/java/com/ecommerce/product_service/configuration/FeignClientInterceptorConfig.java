package com.ecommerce.product_service.configuration;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuration class to define a custom Feign client interceptor.
 * <p>
 * This interceptor ensures that the Authorization token from the current HTTP request
 * is forwarded to any outgoing Feign client calls. This is essential for propagating
 * authentication context (e.g., JWT tokens) between microservices.
 */
@Configuration
@Slf4j
public class FeignClientInterceptorConfig {

    /**
     * Creates a {@link RequestInterceptor} bean that intercepts all Feign client requests.
     * <p>
     * If the current request context exists and has an Authorization token,
     * it will be added to the headers of the outgoing Feign request.
     *
     * @return a custom {@link RequestInterceptor} for Feign clients
     */
    @Bean
    public RequestInterceptor feignClientInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String token = request.getHeader("Authorization");

                if (token != null) {
                    log.debug("🔐 Forwarding Authorization token to Feign client");
                    requestTemplate.header("Authorization", token);
                } else {
                    log.warn("⚠️ Authorization token not found in the request header");
                }
            } else {
                log.warn("⚠️ Request attributes are null. Cannot propagate token.");
            }
        };
    }
}
