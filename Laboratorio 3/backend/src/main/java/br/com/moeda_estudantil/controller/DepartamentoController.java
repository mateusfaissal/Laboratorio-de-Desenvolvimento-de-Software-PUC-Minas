package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.Departamento;
import br.com.moeda_estudantil.model.Professor;
import br.com.moeda_estudantil.service.DepartamentoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@Tag(name = "Departamento", description = "API para gerenciamento de departamentos")
public class DepartamentoController {

  @Autowired
  private DepartamentoService departamentoService;

  @Autowired
  private ProfessorService professorService;

  @GetMapping
  @Operation(summary = "Listar todos os departamentos", description = "Retorna a lista de todos os departamentos cadastrados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de departamentos recuperada com sucesso")
  })
  public ResponseEntity<List<Departamento>> listarTodos() {
    return ResponseEntity.ok(departamentoService.listarTodos());
  }

  @GetMapping("/instituicao/{instituicaoId}")
  @Operation(summary = "Listar departamentos por instituição", description = "Retorna a lista de departamentos de uma instituição específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de departamentos recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Instituição não encontrada")
  })
  public ResponseEntity<List<Departamento>> listarPorInstituicao(
      @Parameter(description = "ID da instituição", required = true) @PathVariable String instituicaoId) {
    return ResponseEntity.ok(departamentoService.listarPorInstituicao(instituicaoId));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar departamento por ID", description = "Retorna um departamento específico com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Departamento encontrado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Departamento não encontrado")
  })
  public ResponseEntity<Departamento> buscarPorId(
      @Parameter(description = "ID do departamento", required = true) @PathVariable String id) {
    return ResponseEntity.ok(departamentoService.buscarPorId(id));
  }

  @GetMapping("/{id}/professores")
  @Operation(summary = "Listar professores por departamento", description = "Retorna a lista de professores de um departamento específico")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de professores recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Departamento não encontrado")
  })
  public ResponseEntity<List<Professor>> listarProfessoresPorDepartamento(
      @Parameter(description = "ID do departamento", required = true) @PathVariable String id) {
    // Verificar se o departamento existe
    departamentoService.buscarPorId(id);
    // Buscar professores do departamento
    return ResponseEntity.ok(professorService.listarPorDepartamento(id));
  }

  @PostMapping("/instituicao/{instituicaoId}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Cadastrar novo departamento", description = "Cria um novo departamento para uma instituição específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Departamento criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Instituição não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Departamento> cadastrar(
      @Parameter(description = "ID da instituição", required = true) @PathVariable String instituicaoId,
      @Parameter(description = "Dados do departamento", required = true) @Valid @RequestBody Departamento departamento) {
    Departamento departamentoCriado = departamentoService.criar(departamento, instituicaoId);
    return ResponseEntity.status(HttpStatus.CREATED).body(departamentoCriado);
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Atualizar departamento", description = "Atualiza os dados de um departamento existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Departamento atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Departamento> atualizar(
      @Parameter(description = "ID do departamento", required = true) @PathVariable String id,
      @Parameter(description = "Dados atualizados do departamento", required = true) @Valid @RequestBody Departamento departamento) {
    return ResponseEntity.ok(departamentoService.atualizar(id, departamento));
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Excluir departamento", description = "Remove um departamento do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Departamento excluído com sucesso"),
      @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
      @ApiResponse(responseCode = "400", description = "Departamento possui professores vinculados")
  })
  public ResponseEntity<Void> deletar(
      @Parameter(description = "ID do departamento", required = true) @PathVariable String id) {
    departamentoService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}