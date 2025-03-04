package com.weblibrary.web;

import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.argumentresolver.LoginUserArgumentResolver;
import com.weblibrary.web.interceptor.AdminCheckInterceptor;
import com.weblibrary.web.interceptor.LoginCheckInterceptor;
import com.weblibrary.web.interceptor.SkipAdminCheckForGetInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserService userService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginUserArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/users/**")
                .addPathPatterns("/books/{id:\\d+}/rent", "/books/{id:\\d+}/return");

        registry.addInterceptor(new SkipAdminCheckForGetInterceptor(userService))
                .order(2)
                .addPathPatterns("/books/{id:\\d+}")
                .addPathPatterns("/users/**");

        registry.addInterceptor(new AdminCheckInterceptor(userService))
                .order(3)
                .addPathPatterns("/books/add", "/books/rentals")
                .addPathPatterns("/users/roles");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
