package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.AlterarSenhaRequest;
import br.com.moeda_estudantil.model.Usuario;
import br.com.moeda_estudantil.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuário", description = "API para gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @GetMapping
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Listar todos os usuários", description = "Retorna a lista de todos os usuários cadastrados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de usuários recuperada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<Usuario>> listarTodos() {
    return ResponseEntity.ok(usuarioService.listarTodos());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.isCurrentUser(#id)")
  @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Usuario> buscarPorId(
      @Parameter(description = "ID do usuário", required = true) @PathVariable String id) {
    return ResponseEntity.ok(usuarioService.buscarPorId(id));
  }

  @GetMapping("/perfil")
  @Operation(summary = "Obter perfil do usuário autenticado", description = "Retorna os dados do usuário autenticado atual")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Perfil recuperado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
  })
  public ResponseEntity<Usuario> buscarPerfilAtual(Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(usuarioService.buscarPorEmail(email));
  }

  @PostMapping("/{id}/alterarSenha")
  @PreAuthorize("@securityService.isCurrentUser(#id)")
  @Operation(summary = "Alterar senha", description = "Altera a senha do usuário autenticado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou nova senha inválida"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  public ResponseEntity<Void> alterarSenha(
      @Parameter(description = "ID do usuário", required = true) @PathVariable String id,
      @Parameter(description = "Dados de alteração de senha", required = true) @Valid @RequestBody AlterarSenhaRequest request) {
    usuarioService.alterarSenha(id, request);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.isCurrentUser(#id)")
  @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Usuario> atualizar(
      @Parameter(description = "ID do usuário", required = true) @PathVariable String id,
      @Parameter(description = "Dados atualizados do usuário", required = true) @Valid @RequestBody Usuario usuario) {
    return ResponseEntity.ok(usuarioService.atualizar(id, usuario));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Void> deletar(
      @Parameter(description = "ID do usuário", required = true) @PathVariable String id) {
    usuarioService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}