package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.Professor;
import br.com.moeda_estudantil.model.TransferenciaAluno;
import br.com.moeda_estudantil.service.ProfessorService;
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
@RequestMapping("/api/professores")
@Tag(name = "Professor", description = "API para gerenciamento de professores")
@SecurityRequirement(name = "bearerAuth")
public class ProfessorController {

  @Autowired
  private ProfessorService professorService;

  @GetMapping
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Listar todos os professores", description = "Retorna a lista de todos os professores cadastrados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de professores recuperada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<Professor>> listarTodos() {
    return ResponseEntity.ok(professorService.listarTodos());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Buscar professor por ID", description = "Retorna um professor com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Professor encontrado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Professor> buscarPorId(
      @Parameter(description = "ID do professor", required = true) @PathVariable String id) {
    return ResponseEntity.ok(professorService.buscarPorId(id));
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Obter dados do professor autenticado", description = "Retorna os dados do professor autenticado atual")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Dados recuperados com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content),
      @ApiResponse(responseCode = "403", description = "Usuário não é professor", content = @Content)
  })
  public ResponseEntity<Professor> buscarProfessorAutenticado(Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(professorService.buscarPorEmail(email));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR') and @securityService.isCurrentUser(#id)")
  @Operation(summary = "Atualizar professor", description = "Atualiza os dados de um professor existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Professor atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Professor> atualizar(
      @Parameter(description = "ID do professor", required = true) @PathVariable String id,
      @Parameter(description = "Dados atualizados do professor", required = true) @Valid @RequestBody Professor professor) {
    return ResponseEntity.ok(professorService.atualizar(id, professor));
  }

  @GetMapping("/saldo")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Consultar saldo de moedas", description = "Retorna o saldo atual de moedas do professor autenticado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Saldo recuperado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content),
      @ApiResponse(responseCode = "403", description = "Usuário não é professor", content = @Content)
  })
  public ResponseEntity<Double> consultarSaldo(Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(professorService.consultarSaldo(email));
  }

  @PostMapping("/enviar-moedas")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Enviar moedas para aluno", description = "Transfere moedas do professor autenticado para um aluno")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Moedas enviadas com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos ou saldo insuficiente"),
      @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<TransferenciaAluno> enviarMoedas(
      @Parameter(description = "Dados da transferência", required = true) @Valid @RequestBody TransferenciaAluno transferenciaAluno,
      Authentication authentication) {
    String email = authentication.getName();
    TransferenciaAluno transferencia = professorService.enviarMoedas(email,
        transferenciaAluno.getAluno().getId(),
        transferenciaAluno.getValor(),
        transferenciaAluno.getMotivoReconhecimento());
    return ResponseEntity.ok(transferencia);
  }

  @GetMapping("/extrato")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Consultar extrato do professor", description = "Retorna o histórico de transações do professor (envio de moedas)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Extrato recuperado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content),
      @ApiResponse(responseCode = "403", description = "Usuário não é professor", content = @Content)
  })
  public ResponseEntity<List<TransferenciaAluno>> consultarExtrato(Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(professorService.consultarExtrato(email));
  }
}