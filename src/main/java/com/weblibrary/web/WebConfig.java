package com.weblibrary.web;

import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.argumentresolver.LoginUserArgumentResolver;
import com.weblibrary.web.interceptor.RestAdminCheckInterceptor;
import com.weblibrary.web.interceptor.RestLoginCheckInterceptor;
import com.weblibrary.web.interceptor.SsrAdminCheckInterceptor;
import com.weblibrary.web.interceptor.SsrLoginCheckInterceptor;
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
        registry.addInterceptor(new SsrLoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/admin/**");

        registry.addInterceptor(new RestLoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/books/**");

        registry.addInterceptor(new SsrAdminCheckInterceptor(userService))
                .order(3)
                .addPathPatterns("/admin/**");

        registry.addInterceptor(new RestAdminCheckInterceptor(userService))
                .order(4)
                .addPathPatterns("/books/{id:\\d+}", "/books/add");

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
