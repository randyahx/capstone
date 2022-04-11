package com.randyahx.authorizationserver.config;

import com.randyahx.authorizationserver.service.AuthenticationProvider;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity @RequiredArgsConstructor
public class DefaultSecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    // All requests require authentication
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults());
        return http.build();
    }

    // Authentication provider
    @Autowired
    public void bindAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }
}
