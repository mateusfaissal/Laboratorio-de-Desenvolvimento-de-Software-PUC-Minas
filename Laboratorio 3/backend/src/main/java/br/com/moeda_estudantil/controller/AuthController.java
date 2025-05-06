package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.LoginRequest;
import br.com.moeda_estudantil.model.LoginResponse;
import br.com.moeda_estudantil.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "API para autenticação de usuários")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  @Operation(summary = "Autenticar usuário", description = "Autentica um usuário com email e senha, retornando um token JWT")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
      @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content)
  })
  public ResponseEntity<LoginResponse> login(
      @Parameter(description = "Credenciais de login", required = true) @Valid @RequestBody LoginRequest loginRequest) {
    LoginResponse loginResponse = authService.login(loginRequest);
    return ResponseEntity.ok(loginResponse);
  }
}