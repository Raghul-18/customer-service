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

    // Remove the manual constructor - @RequiredArgsConstructor handles this

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/api/customers/register")
                .addPathPatterns("/api/customers/{customerId}")
                .addPathPatterns("/api/customers/{customerId}/**")
                .addPathPatterns("/api/customers/admin/**")
                .addPathPatterns("/api/customers/user/{userId}/customer-id") // NEW
                .addPathPatterns("/api/customers/{customerId}/verify-ownership/{userId}"); // NEW
    }
}