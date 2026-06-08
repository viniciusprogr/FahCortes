package com.barbearia.fahcortes.infra.config;

import com.barbearia.fahcortes.infra.security.SecurityFilter;
import com.barbearia.fahcortes.infra.security.UsuarioDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter,
                                                   CorsConfigurationSource corsConfigurationSource) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();

                    req.requestMatchers(HttpMethod.GET, "/servico/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/servico").permitAll();

                    req.requestMatchers(HttpMethod.GET, "/barbeiros/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/barbeiros").permitAll();
                    req.requestMatchers(HttpMethod.PATCH, "/barbeiros/*/curtir").permitAll();

                    req.requestMatchers(HttpMethod.GET, "/produtos/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/produtos").permitAll();

                    req.requestMatchers(HttpMethod.GET, "/unidades/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/unidades").permitAll();

                    req.requestMatchers(HttpMethod.GET, "/planos/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/planos").permitAll();

                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UsuarioDetailService usuarioDetailService,
                                                       PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(usuarioDetailService).passwordEncoder(passwordEncoder);
        return builder.build();
    }
}