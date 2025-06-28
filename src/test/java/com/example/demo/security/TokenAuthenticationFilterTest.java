package com.example.demo.security;

import com.example.demo.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;

import static org.mockito.Mockito.*;

class TokenAuthenticationFilterTest {

    private TokenUtils tokenUtils;
    private SecurityContextRepository contextRepository;
    private TokenAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        tokenUtils = mock(TokenUtils.class);
        contextRepository = mock(SecurityContextRepository.class);
        filter = new TokenAuthenticationFilter(tokenUtils, contextRepository);
    }

    @Test
    void testDoFilterInternal_NoToken() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("token")).thenReturn(null);

        filter.doFilterInternal(req, res, chain);

        verify(chain).doFilter(req, res);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(req.getHeader("token")).thenReturn("valid-token");
        when(tokenUtils.getTokenUsername("valid-token")).thenReturn("user");

        filter.doFilterInternal(req, res, chain);
        verify(contextRepository).saveContext(any(), eq(req), eq(res));
        verify(chain).doFilter(req, res);
    }
}
