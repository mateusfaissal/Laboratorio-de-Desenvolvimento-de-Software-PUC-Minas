package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.Curso;
import br.com.moeda_estudantil.model.Instituicao;
import br.com.moeda_estudantil.service.CursoService;
import br.com.moeda_estudantil.service.InstituicaoService;
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
@RequestMapping("/api")
@Tag(name = "Instituição e Curso", description = "API para gerenciamento de instituições e cursos")
public class CursoController {

  @Autowired
  private InstituicaoService instituicaoService;

  @Autowired
  private CursoService cursoService;

  // Endpoints de Instituição

  @GetMapping("/instituicoes")
  @Operation(summary = "Listar todas as instituições", description = "Retorna a lista de todas as instituições cadastradas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de instituições recuperada com sucesso")
  })
  public ResponseEntity<List<Instituicao>> listarInstituicoes() {
    return ResponseEntity.ok(instituicaoService.listarTodas());
  }

  @GetMapping("/instituicoes/{id}")
  @Operation(summary = "Buscar instituição por ID", description = "Retorna uma instituição específica com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Instituição encontrada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Instituição não encontrada")
  })
  public ResponseEntity<Instituicao> buscarInstituicaoPorId(
      @Parameter(description = "ID da instituição", required = true) @PathVariable String id) {
    return ResponseEntity.ok(instituicaoService.buscarPorId(id));
  }

  @PostMapping("/instituicoes")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Cadastrar nova instituição", description = "Cria uma nova instituição no sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Instituição criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Instituicao> cadastrarInstituicao(
      @Parameter(description = "Dados da instituição", required = true) @Valid @RequestBody Instituicao instituicao) {
    Instituicao instituicaoCriada = instituicaoService.criar(instituicao);
    return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoCriada);
  }

  @PutMapping("/instituicoes/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Atualizar instituição", description = "Atualiza os dados de uma instituição existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Instituição atualizada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Instituição não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Instituicao> atualizarInstituicao(
      @Parameter(description = "ID da instituição", required = true) @PathVariable String id,
      @Parameter(description = "Dados atualizados da instituição", required = true) @Valid @RequestBody Instituicao instituicao) {
    return ResponseEntity.ok(instituicaoService.atualizar(id, instituicao));
  }

  @DeleteMapping("/instituicoes/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Excluir instituição", description = "Remove uma instituição do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Instituição excluída com sucesso"),
      @ApiResponse(responseCode = "404", description = "Instituição não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Void> deletarInstituicao(
      @Parameter(description = "ID da instituição", required = true) @PathVariable String id) {
    instituicaoService.deletar(id);
    return ResponseEntity.noContent().build();
  }

  // Endpoints de Curso

  @GetMapping("/cursos")
  @Operation(summary = "Listar todos os cursos", description = "Retorna a lista de todos os cursos cadastrados")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de cursos recuperada com sucesso")
  })
  public ResponseEntity<List<Curso>> listarCursos() {
    return ResponseEntity.ok(cursoService.listarTodos());
  }

  @GetMapping("/instituicoes/{instituicaoId}/cursos")
  @Operation(summary = "Listar cursos por instituição", description = "Retorna a lista de cursos de uma instituição específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de cursos recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Instituição não encontrada")
  })
  public ResponseEntity<List<Curso>> listarCursosPorInstituicao(
      @Parameter(description = "ID da instituição", required = true) @PathVariable String instituicaoId) {
    return ResponseEntity.ok(cursoService.listarPorInstituicao(instituicaoId));
  }

  @GetMapping("/cursos/{id}")
  @Operation(summary = "Buscar curso por ID", description = "Retorna um curso específico com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Curso encontrado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Curso não encontrado")
  })
  public ResponseEntity<Curso> buscarCursoPorId(
      @Parameter(description = "ID do curso", required = true) @PathVariable String id) {
    return ResponseEntity.ok(cursoService.buscarPorId(id));
  }

  @PostMapping("/instituicoes/{instituicaoId}/cursos")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Cadastrar novo curso", description = "Cria um novo curso para uma instituição específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Instituição não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Curso> cadastrarCurso(
      @Parameter(description = "ID da instituição", required = true) @PathVariable String instituicaoId,
      @Parameter(description = "Dados do curso", required = true) @Valid @RequestBody Curso curso) {
    Curso cursoCriado = cursoService.criar(curso, instituicaoId);
    return ResponseEntity.status(HttpStatus.CREATED).body(cursoCriado);
  }

  @PutMapping("/cursos/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Atualizar curso", description = "Atualiza os dados de um curso existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Curso> atualizarCurso(
      @Parameter(description = "ID do curso", required = true) @PathVariable String id,
      @Parameter(description = "Dados atualizados do curso", required = true) @Valid @RequestBody Curso curso) {
    return ResponseEntity.ok(cursoService.atualizar(id, curso));
  }

  @DeleteMapping("/cursos/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('PROFESSOR')")
  @Operation(summary = "Excluir curso", description = "Remove um curso do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Curso excluído com sucesso"),
      @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Void> deletarCurso(
      @Parameter(description = "ID do curso", required = true) @PathVariable String id) {
    cursoService.deletar(id);
    return ResponseEntity.noContent().build();
  }
}