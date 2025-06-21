package com.example.demo;

import com.example.demo.security.TokenAuthenticationFilter;
import com.example.demo.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenAuthenticationFilterTests {

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private SecurityContextRepository securityContextRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @InjectMocks
    private TokenAuthenticationFilter filter;

    @Test
    void testDoFilterInternal_NoToken() throws Exception {
        when(request.getHeader("token")).thenReturn(null);
        filter.doFilterInternal(request, response, chain);
        verify(chain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_WithToken() throws Exception {
        when(request.getHeader("token")).thenReturn("valid-token");
        when(tokenUtils.getTokenUsername("valid-token")).thenReturn("user");

        filter.doFilterInternal(request, response, chain);

        verify(securityContextRepository).saveContext(any(), eq(request), eq(response));
        verify(chain).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws Exception {
        when(request.getHeader("token")).thenReturn("invalid-token");
        when(tokenUtils.getTokenUsername("invalid-token")).thenThrow(new RuntimeException("Invalid token"));

        filter.doFilterInternal(request, response, chain);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
