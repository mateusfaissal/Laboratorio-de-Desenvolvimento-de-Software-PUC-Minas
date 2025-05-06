package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.ResgateCupom;
import br.com.moeda_estudantil.model.Transacao;
import br.com.moeda_estudantil.model.TransferenciaAluno;
import br.com.moeda_estudantil.service.TransacaoService;
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
@RequestMapping("/api/transacoes")
@Tag(name = "Transação", description = "API para gerenciamento de transações (transferências e resgates)")
@SecurityRequirement(name = "bearerAuth")
public class TransacaoController {

  @Autowired
  private TransacaoService transacaoService;

  @GetMapping("/extrato")
  @Operation(summary = "Consultar extrato do usuário autenticado", description = "Retorna o histórico de transações do usuário atual")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Extrato recuperado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
  })
  public ResponseEntity<List<Transacao>> consultarExtratoUsuarioAutenticado(Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(transacaoService.buscarTransacoesPorUsuario(email));
  }

  @GetMapping("/transferencias/aluno/{alunoId}")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.isCurrentUser(#alunoId)")
  @Operation(summary = "Listar transferências de um aluno", description = "Retorna a lista de transferências recebidas por um aluno específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de transferências recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<TransferenciaAluno>> listarTransferenciasAluno(
      @Parameter(description = "ID do aluno", required = true) @PathVariable String alunoId) {
    return ResponseEntity.ok(transacaoService.buscarTransferenciasParaAluno(alunoId));
  }

  @GetMapping("/transferencias/professor/{professorId}")
  @PreAuthorize("hasRole('PROFESSOR') and @securityService.isCurrentUser(#professorId)")
  @Operation(summary = "Listar transferências de um professor", description = "Retorna a lista de transferências enviadas por um professor específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de transferências recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Professor não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<TransferenciaAluno>> listarTransferenciasProfessor(
      @Parameter(description = "ID do professor", required = true) @PathVariable String professorId) {
    return ResponseEntity.ok(transacaoService.buscarTransferenciasDoProfessor(professorId));
  }

  @GetMapping("/cupons")
  @PreAuthorize("hasRole('ALUNO')")
  @Operation(summary = "Listar cupons do aluno autenticado", description = "Retorna a lista de cupons (vantagens resgatadas) do aluno atual")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de cupons recuperada com sucesso"),
      @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content),
      @ApiResponse(responseCode = "403", description = "Usuário não é aluno", content = @Content)
  })
  public ResponseEntity<List<ResgateCupom>> listarCuponsDoAlunoAutenticado(Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(transacaoService.buscarCuponsPorAluno(email));
  }

  @GetMapping("/cupons/{cupomId}")
  @PreAuthorize("hasRole('ALUNO') or hasRole('EMPRESA')")
  @Operation(summary = "Buscar detalhes de um cupom", description = "Retorna os detalhes de um cupom específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cupom encontrado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<ResgateCupom> buscarCupomPorId(
      @Parameter(description = "ID do cupom", required = true) @PathVariable String cupomId,
      Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(transacaoService.buscarCupomPorId(cupomId, email));
  }

  @GetMapping("/cupons/empresa/{empresaId}")
  @PreAuthorize("hasRole('EMPRESA') and @securityService.isCurrentUser(#empresaId)")
  @Operation(summary = "Listar cupons resgatados em uma empresa", description = "Retorna a lista de cupons resgatados nas vantagens de uma empresa específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de cupons recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<ResgateCupom>> listarCuponsPorEmpresa(
      @Parameter(description = "ID da empresa", required = true) @PathVariable String empresaId) {
    return ResponseEntity.ok(transacaoService.buscarCuponsPorEmpresa(empresaId));
  }

  @PostMapping("/cupons/{cupomId}/validar")
  @PreAuthorize("hasRole('EMPRESA')")
  @Operation(summary = "Validar um cupom", description = "Marca um cupom como utilizado (validado) pelo aluno na empresa")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cupom validado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Cupom já utilizado ou inválido"),
      @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<ResgateCupom> validarCupom(
      @Parameter(description = "ID do cupom", required = true) @PathVariable String cupomId,
      @Parameter(description = "Código de confirmação", required = true) @RequestParam String codigoConfirmacao,
      Authentication authentication) {
    String email = authentication.getName();
    return ResponseEntity.ok(transacaoService.validarCupom(cupomId, codigoConfirmacao, email));
  }
}