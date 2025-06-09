package com.example.demo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenUtils {

  private final SecretKey secretKey;
  private final long tokenDuration;

  public TokenUtils(@Value("${spring.jjwt.secret}") String tokenSecret,
                    @Value("${spring.jjwt.duration}") long tokenDuration) {
    this.tokenDuration = tokenDuration;
    this.secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes());
  }

  public String generateToken(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + tokenDuration))
        .signWith(secretKey)
        .compact();
  }

  public String getTokenUsername(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

}
