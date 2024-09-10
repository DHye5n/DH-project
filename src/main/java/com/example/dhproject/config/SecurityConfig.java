package com.example.dhproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/css/**", "/js/user/**", "/images/**", "/plugins/**").permitAll()
                                .antMatchers(
                                        "/api/pubic/members/**",
                                        "/register",
                                        "/api/public/members/send-verification-code",
                                        "/api/public/members/verify-code",
                                        "/api/public/members/check-email").permitAll()
                                .antMatchers("/api/admin/members/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Custom login page
                                .permitAll()
                )
                .logout(LogoutConfigurer::permitAll
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
