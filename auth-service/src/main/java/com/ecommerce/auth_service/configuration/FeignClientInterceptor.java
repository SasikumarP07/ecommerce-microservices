package com.ecommerce.auth_service.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Interceptor for Feign clients to propagate the Authorization header
 * from the current HTTP request to downstream services.
 * This ensures that JWT tokens are passed along when making Feign calls,
 * maintaining authentication and authorization across microservices.
 */
@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    /**
     * Applies the interceptor logic to copy the Authorization header
     * from the current request into the outgoing Feign client request.
     *
     * @param template the Feign request template to be modified
     */
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null) {
                template.header("Authorization", authHeader);
                log.info("🔐 Forwarding Authorization header: {}", authHeader);
            } else {
                log.warn("⚠️ No Authorization header found in the current request");
            }
        } else {
            log.warn("⚠️ ServletRequestAttributes is null; Feign client request may not be within HTTP context");
        }
    }
}
