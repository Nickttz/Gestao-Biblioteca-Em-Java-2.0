package com.project.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desativa CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/cadastro", "/usuarios").permitAll() // Libera essas rotas
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/usuarios/**").permitAll() // libera DELETE
                .anyRequest().authenticated() // Protege o resto
            );
        return http.build();
    }
}