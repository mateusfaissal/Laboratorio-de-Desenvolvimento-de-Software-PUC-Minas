package br.com.moeda_estudantil.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);

      if (StringUtils.hasText(jwt)) {
        if (tokenProvider.validateToken(jwt)) {
          Authentication authentication = tokenProvider.getAuthentication(jwt);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          throw new AuthenticationException("Token inválido") {};
        }
      }
    } catch (Exception ex) {
      logger.error("Não foi possível autenticar o usuário", ex);
      SecurityContextHolder.clearContext();
      authenticationEntryPoint.commence(request, response, 
          new AuthenticationException("Não foi possível autenticar o usuário: " + ex.getMessage()) {});
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}