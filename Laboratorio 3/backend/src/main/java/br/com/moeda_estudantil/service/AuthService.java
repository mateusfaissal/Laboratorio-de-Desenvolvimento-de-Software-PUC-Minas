package br.com.moeda_estudantil.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.moeda_estudantil.model.LoginRequest;
import br.com.moeda_estudantil.model.LoginResponse;
import br.com.moeda_estudantil.model.Usuario;
import br.com.moeda_estudantil.repository.UsuarioRepository;
import br.com.moeda_estudantil.security.JwtTokenProvider;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final UsuarioRepository usuarioRepository;

  public AuthService(AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider,
      UsuarioRepository usuarioRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    this.usuarioRepository = usuarioRepository;
  }

  public LoginResponse login(LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getEmail(),
              loginRequest.getSenha()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Gerar JWT token
      String jwt = jwtTokenProvider.generateToken(authentication);

      // Buscar informações adicionais do usuário
      Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
          .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

      // Construir resposta
      LoginResponse response = new LoginResponse();
      response.setToken(jwt);
      response.setId(usuario.getId());
      response.setNome(usuario.getNome());
      response.setEmail(usuario.getEmail());
      response.setTipo(usuario.getTipo());

      return response;
    } catch (AuthenticationException e) {
      throw new RuntimeException("Credenciais inválidas");
    }
  }
}