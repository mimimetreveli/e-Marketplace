package com.example.demo.security;

import com.example.demo.util.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@AllArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private static final String TOKEN_HEADER = "token";

  private final TokenUtils tokenUtils;
  private final SecurityContextRepository securityContextRepository;

  public TokenAuthenticationFilter(TokenUtils tokenUtils, SecurityContextRepository securityContextRepository) {
    this.tokenUtils = tokenUtils;
    this.securityContextRepository = securityContextRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    var token = request.getHeader(TOKEN_HEADER);
    if (token == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      var username = tokenUtils.getTokenUsername(token);
      var authToken = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
      var context = SecurityContextHolder.getContext();
      context.setAuthentication(authToken);
      SecurityContextHolder.setContext(context);
      securityContextRepository.saveContext(context, request, response);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().println(e.getMessage());
      return;
    }

    filterChain.doFilter(request, response);
  }
}
