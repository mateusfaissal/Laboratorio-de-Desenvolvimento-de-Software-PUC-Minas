package br.com.moeda_estudantil.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

  @Value("${app.jwt.secret:JwtSecretKey-MustBeAtLeast32CharactersLong-ForSecurity}")
  private String jwtSecret;

  @Value("${app.jwt.expiration:86400000}")
  private int jwtExpirationMs;

  @Value("${app.jwt.authorities-key:auth}")
  private String authoritiesKey;

  /**
   * Gera um token JWT para o usuário autenticado
   */
  public String generateToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    logger.debug("Gerando token para usuário: {} com authorities: {}", authentication.getName(), authorities);

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(authoritiesKey, authorities)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Extrai o usuário do token JWT
   */
  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    String authorities = claims.get(authoritiesKey, String.class);
    logger.debug("Extraindo authorities do token: {}", authorities);

    Collection<? extends GrantedAuthority> grantedAuthorities = Arrays.stream(authorities.split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    User principal = new User(claims.getSubject(), "", grantedAuthorities);
    logger.debug("Usuário autenticado: {} com roles: {}", principal.getUsername(), grantedAuthorities);

    return new UsernamePasswordAuthenticationToken(principal, token, grantedAuthorities);
  }

  /**
   * Valida o token JWT
   */
  public boolean validateToken(String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(getSigningKey())
          .build()
          .parseClaimsJws(token)
          .getBody();

      // Verificar se o token expirou
      if (claims.getExpiration().before(new Date())) {
        logger.error("Token expirado em: {}", claims.getExpiration());
        return false;
      }

      // Verificar se o token tem as claims necessárias
      if (!claims.containsKey(authoritiesKey)) {
        logger.error("Token não contém a claim de authorities");
        return false;
      }

      logger.debug("Token válido para usuário: {} com authorities: {}", 
          claims.getSubject(), claims.get(authoritiesKey));
      return true;
    } catch (ExpiredJwtException e) {
      logger.error("Token expirado: {}", e.getMessage());
      return false;
    } catch (JwtException | IllegalArgumentException e) {
      logger.error("Token inválido: {}", e.getMessage());
      return false;
    }
  }

  /**
   * Obtém o nome de usuário do token
   */
  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  /**
   * Cria uma chave de assinatura baseada no segredo JWT
   */
  private Key getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }
}