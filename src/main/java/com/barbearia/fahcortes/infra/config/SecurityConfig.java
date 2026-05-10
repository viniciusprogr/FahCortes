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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityFilter securityFilter) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                request -> HttpMethod.POST.matches(request.getMethod()) && "/login".equals(request.getServletPath()),
                                request -> HttpMethod.POST.matches(request.getMethod()) && "/usuarios".equals(request.getServletPath()),
                                request -> HttpMethod.GET.matches(request.getMethod()) && request.getServletPath().startsWith("/servico"),
                                request -> request.getHeader("Authorization") != null
                        )
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/servico").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/servico/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/usuarios").authenticated();
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager é responsável por gerenciar a autenticação.
     * Ele usa o UsuarioDetailService para carregar o usuário e compara as senhas.
     * Isso centraliza a lógica de autenticação no Spring Security.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UsuarioDetailService usuarioDetailService, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(usuarioDetailService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
