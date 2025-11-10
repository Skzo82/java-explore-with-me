package ru.practicum.main.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.practicum.main.web.HitInterceptor;

/* # Регистрация перехватчиков MVC */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final HitInterceptor hitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hitInterceptor)
                .addPathPatterns("/**")                 // # перехватываем все пути
                .excludePathPatterns("/actuator/**",    // # исключаем служебные
                        "/error");
    }
}