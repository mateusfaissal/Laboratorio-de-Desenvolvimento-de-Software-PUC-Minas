package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.Notificacao;
import br.com.moeda_estudantil.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@Tag(name = "Notificação", description = "API para gerenciamento de notificações")
@SecurityRequirement(name = "bearerAuth")
public class NotificacaoController {

  @Autowired
  private NotificacaoService notificacaoService;

  @GetMapping
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Listar todas as notificações", description = "Retorna a lista de todas as notificações do sistema (acesso administrativo)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de notificações recuperada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<Notificacao>> listarTodas() {
    return ResponseEntity.ok(notificacaoService.listarTodas());
  }

  @GetMapping("/minhas")
  @Operation(summary = "Listar notificações do usuário atual", description = "Retorna a lista de notificações destinadas ao usuário autenticado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de notificações recuperada com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
  })
  public ResponseEntity<List<Notificacao>> listarMinhasNotificacoes(Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(notificacaoService.listarPorDestinatario(email));
  }

  @GetMapping("/status/{status}")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Listar notificações por status", description = "Retorna a lista de notificações com um determinado status")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de notificações recuperada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
      @ApiResponse(responseCode = "400", description = "Status inválido")
  })
  public ResponseEntity<List<Notificacao>> listarPorStatus(
      @Parameter(description = "Status da notificação (PENDENTE, ENVIADO, FALHA)", required = true) @PathVariable Notificacao.StatusNotificacao status) {
    return ResponseEntity.ok(notificacaoService.listarPorStatus(status));
  }

  @GetMapping("/resgate/{resgateId}")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.verificarPermissaoResgate(#resgateId)")
  @Operation(summary = "Listar notificações por resgate", description = "Retorna a lista de notificações associadas a um determinado resgate")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de notificações recuperada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
      @ApiResponse(responseCode = "404", description = "Resgate não encontrado")
  })
  public ResponseEntity<List<Notificacao>> listarPorResgate(
      @Parameter(description = "ID do resgate", required = true) @PathVariable String resgateId) {
    return ResponseEntity.ok(notificacaoService.listarPorResgate(resgateId));
  }
}