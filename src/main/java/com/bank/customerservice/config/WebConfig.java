package com.bank.customerservice.config;

import com.bank.customerservice.security.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/customers/register") // ✅ Apply JWT to register endpoint
                .addPathPatterns("/api/customers/{customerId}") // Existing protected endpoints
                .addPathPatterns("/api/customers/{customerId}/**")
                .addPathPatterns("/api/customers/admin/**"); // Admin endpoints
    }
}
