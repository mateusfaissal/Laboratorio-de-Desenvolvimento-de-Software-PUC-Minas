package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.EmpresaParceira;
import br.com.moeda_estudantil.service.EmpresaParceiraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@Tag(name = "Empresa Parceira", description = "API para gerenciamento de empresas parceiras")
public class EmpresaParceiraController {

  @Autowired
  private EmpresaParceiraService empresaService;

  @GetMapping
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR') or hasRole('ALUNO')")
  @Operation(summary = "Listar todas as empresas", description = "Retorna a lista de todas as empresas parceiras cadastradas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de empresas recuperada com sucesso"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<EmpresaParceira>> listarTodas() {
    return ResponseEntity.ok(empresaService.listarTodas());
  }

  @GetMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR') or hasRole('ALUNO') or @securityService.isCurrentUser(#id)")
  @Operation(summary = "Buscar empresa por ID", description = "Retorna uma empresa parceira com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Empresa encontrada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<EmpresaParceira> buscarPorId(
      @Parameter(description = "ID da empresa", required = true) @PathVariable String id) {
    return ResponseEntity.ok(empresaService.buscarPorId(id));
  }

  @PostMapping("/cadastro")
  @Operation(summary = "Cadastrar nova empresa", description = "Cria uma nova empresa parceira no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "409", description = "Empresa já cadastrada com este email/CNPJ")
  })
  public ResponseEntity<EmpresaParceira> cadastrar(
      @Parameter(description = "Dados da empresa", required = true) @Valid @RequestBody EmpresaParceira empresa) {
    EmpresaParceira empresaCriada = empresaService.cadastrar(empresa);
    return ResponseEntity.status(HttpStatus.CREATED).body(empresaCriada);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.isCurrentUser(#id)")
  @Operation(summary = "Atualizar empresa", description = "Atualiza os dados de uma empresa parceira existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<EmpresaParceira> atualizar(
      @Parameter(description = "ID da empresa", required = true) @PathVariable String id,
      @Parameter(description = "Dados atualizados da empresa", required = true) @Valid @RequestBody EmpresaParceira empresa) {
    return ResponseEntity.ok(empresaService.atualizar(id, empresa));
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Excluir empresa", description = "Remove uma empresa parceira do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Empresa excluída com sucesso"),
      @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Void> deletar(
      @Parameter(description = "ID da empresa", required = true) @PathVariable String id) {
    empresaService.deletar(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/resgates")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("@securityService.isCurrentUser(#id)")
  @Operation(summary = "Listar resgates da empresa", description = "Retorna a lista de resgates (cupons) realizados nas vantagens desta empresa")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de resgates recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<List<Object>> listarResgates(
      @Parameter(description = "ID da empresa", required = true) @PathVariable String id) {
    // Aqui você chamaria um serviço para buscar os resgates da empresa
    // Por exemplo: return
    // ResponseEntity.ok(resgateService.buscarResgatesPorEmpresa(id));
    // Retornando um objeto vazio temporariamente
    return ResponseEntity.ok(List.of());
  }
}