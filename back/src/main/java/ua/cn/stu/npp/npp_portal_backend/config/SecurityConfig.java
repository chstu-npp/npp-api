package ua.cn.stu.npp.npp_portal_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Тимчасова конфігурація Security для тестування API в Postman.
 * УВАГА: Використовувати тільки для розробки!
 *
 * Для продакшену потрібно буде:
 * - Додати JWT автентифікацію
 * - Налаштувати role-based авторизацію
 * - Додати CSRF захист (або правильно його вимкнути для REST API)
 */
@Configuration
@EnableWebSecurity
@Profile("dev") // Працює тільки з профілем dev
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Вимикаємо CSRF для REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/**").permitAll() // Дозволяємо всі запити до API
                        .requestMatchers("/error").permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}