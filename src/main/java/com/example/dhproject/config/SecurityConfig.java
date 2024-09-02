package com.example.dhproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/css/**", "/js/user/**", "/images/**", "/plugins/**").permitAll()
                                .antMatchers("/api/members/**", "/register", "/api/members/send-verification-code").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Custom login page
                                .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                )
                .csrf(csrf -> csrf
                        .ignoringAntMatchers("/api/members/register", "/api/members/send-verification-code", "/api/members/verify-code")
                );
        return http.build();
    }
}
