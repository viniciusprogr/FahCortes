package com.barbearia.fahcortes.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UsuarioDetailService usuarioDetailService;

    public SecurityFilter(TokenService tokenService, UsuarioDetailService usuarioDetailService) {
        this.tokenService = tokenService;
        this.usuarioDetailService = usuarioDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recuperarToken(request);
        if (token != null) {
            var email = tokenService.validarToken(token);
            if (!email.isEmpty()) {
                /**
                 * 1. Carregamos o UsuarioDetails usando UsuarioDetailService
                 * 2. UsuarioDetails já tem as autoridades (roles) configuradas
                 * 3. Criamos o objeto de autenticação do Spring Security
                 * 4. Definimos no contexto de segurança para usar em @PreAuthorize, etc
                 */
                var usuarioDetails = usuarioDetailService.loadUserByUsername(email);
                var authentication = new UsernamePasswordAuthenticationToken(
                        usuarioDetails,
                        null,
                        usuarioDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
