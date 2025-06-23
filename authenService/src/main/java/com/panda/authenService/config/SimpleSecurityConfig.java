package com.panda.authenService.config;

import com.panda.authenService.security.InternalAuthenticationFilter;
import com.panda.authenService.security.JwtContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SimpleSecurityConfig {

    private final InternalAuthenticationFilter internalAuthenticationFilter;
    private final JwtContextFilter jwtContextFilter;

    public SimpleSecurityConfig(InternalAuthenticationFilter internalAuthenticationFilter,
                               JwtContextFilter jwtContextFilter) {
        this.internalAuthenticationFilter = internalAuthenticationFilter;
        this.jwtContextFilter = jwtContextFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/health").permitAll()
                .anyRequest().authenticated()
            )
            // Filter order: Internal Token Validation -> JWT Context Storage
            .addFilterBefore(internalAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(jwtContextFilter, InternalAuthenticationFilter.class)
            .build();
    }
}
