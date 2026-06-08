package com.barbearia.fahcortes.infra.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class LoginRateLimitFilter implements Filter {

    private static final int MAX_TENTATIVAS = 5;
    private static final long JANELA_MS = 60_000;

    private final ConcurrentHashMap<String, Deque<Long>> tentativas = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if ("POST".equalsIgnoreCase(request.getMethod()) && "/login".equals(request.getServletPath())) {
            String ip = request.getRemoteAddr();
            long agora = System.currentTimeMillis();

            Deque<Long> timestamps = tentativas.computeIfAbsent(ip, k -> new ArrayDeque<>());
            synchronized (timestamps) {
                while (!timestamps.isEmpty() && agora - timestamps.peekFirst() > JANELA_MS) {
                    timestamps.pollFirst();
                }
                if (timestamps.size() >= MAX_TENTATIVAS) {
                    HttpServletResponse response = (HttpServletResponse) res;
                    response.setStatus(429);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(
                            "{\"code\":\"RATE_LIMIT_EXCEEDED\",\"message\":\"Muitas tentativas de login. Aguarde 1 minuto antes de tentar novamente.\"}"
                    );
                    return;
                }
                timestamps.addLast(agora);
            }
        }
        chain.doFilter(req, res);
    }
}
