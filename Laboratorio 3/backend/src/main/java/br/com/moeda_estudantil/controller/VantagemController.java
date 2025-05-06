package br.com.moeda_estudantil.controller;

import br.com.moeda_estudantil.model.ResgateCupom;
import br.com.moeda_estudantil.model.Vantagem;
import br.com.moeda_estudantil.service.VantagemService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/vantagens")
@Tag(name = "Vantagem", description = "API para gerenciamento de vantagens oferecidas pelas empresas parceiras")
public class VantagemController {

  @Autowired
  private VantagemService vantagemService;

  @GetMapping
  @Operation(summary = "Listar todas as vantagens", description = "Retorna a lista de todas as vantagens disponíveis")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de vantagens recuperada com sucesso")
  })
  public ResponseEntity<List<Vantagem>> listarTodas() {
    return ResponseEntity.ok(vantagemService.listarTodas());
  }

  @GetMapping("/empresa/{empresaId}")
  @Operation(summary = "Listar vantagens por empresa", description = "Retorna a lista de vantagens oferecidas por uma empresa específica")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de vantagens recuperada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
  })
  public ResponseEntity<List<Vantagem>> listarPorEmpresa(
      @Parameter(description = "ID da empresa", required = true) @PathVariable String empresaId) {
    return ResponseEntity.ok(vantagemService.listarPorEmpresa(empresaId));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar vantagem por ID", description = "Retorna uma vantagem específica com base no ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vantagem encontrada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Vantagem não encontrada")
  })
  public ResponseEntity<Vantagem> buscarPorId(
      @Parameter(description = "ID da vantagem", required = true) @PathVariable String id) {
    return ResponseEntity.ok(vantagemService.buscarPorId(id));
  }

  @PostMapping("/empresa/{empresaId}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('EMPRESA') and @securityService.isEmpresaOwner(#empresaId)")
  @Operation(summary = "Cadastrar nova vantagem", description = "Cria uma nova vantagem oferecida pela empresa")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Vantagem criada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Vantagem> cadastrar(
      @Parameter(description = "ID da empresa", required = true) @PathVariable String empresaId,
      @Parameter(description = "Dados da vantagem", required = true) @Valid @RequestBody Vantagem vantagem) {
    Vantagem vantagemCriada = vantagemService.cadastrar(vantagem, empresaId);
    return ResponseEntity.status(HttpStatus.CREATED).body(vantagemCriada);
  }

  @PostMapping("/{id}/foto")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('EMPRESA') and @securityService.isVantagemOwner(#id)")
  @Operation(summary = "Adicionar foto à vantagem", description = "Faz upload de uma foto para a vantagem")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Foto adicionada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Arquivo inválido"),
      @ApiResponse(responseCode = "404", description = "Vantagem não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Vantagem> adicionarFoto(
      @Parameter(description = "ID da vantagem", required = true) @PathVariable String id,
      @Parameter(description = "Arquivo de foto", required = true) @RequestParam("foto") MultipartFile foto) {
    return ResponseEntity.ok(vantagemService.adicionarFoto(id, foto));
  }

  @PutMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('EMPRESA') and @securityService.isVantagemOwner(#id)")
  @Operation(summary = "Atualizar vantagem", description = "Atualiza os dados de uma vantagem existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vantagem atualizada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "404", description = "Vantagem não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Vantagem> atualizar(
      @Parameter(description = "ID da vantagem", required = true) @PathVariable String id,
      @Parameter(description = "Dados atualizados da vantagem", required = true) @Valid @RequestBody Vantagem vantagem) {
    return ResponseEntity.ok(vantagemService.atualizar(id, vantagem));
  }

  @DeleteMapping("/{id}")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('EMPRESA') and @securityService.isVantagemOwner(#id)")
  @Operation(summary = "Excluir vantagem", description = "Remove uma vantagem do sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Vantagem excluída com sucesso"),
      @ApiResponse(responseCode = "404", description = "Vantagem não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<Void> deletar(
      @Parameter(description = "ID da vantagem", required = true) @PathVariable String id) {
    vantagemService.deletar(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/resgatar")
  @SecurityRequirement(name = "bearerAuth")
  @PreAuthorize("hasRole('ALUNO')")
  @Operation(summary = "Resgatar vantagem", description = "O aluno autenticado resgata uma vantagem, trocando moedas por um cupom")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Vantagem resgatada com sucesso"),
      @ApiResponse(responseCode = "400", description = "Saldo insuficiente"),
      @ApiResponse(responseCode = "404", description = "Vantagem não encontrada"),
      @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
  })
  public ResponseEntity<ResgateCupom> resgatar(
      @Parameter(description = "ID da vantagem", required = true) @PathVariable String id,
      Authentication authentication) {
    String email = authentication.getName();
    ResgateCupom resgate = vantagemService.resgatar(id, email);
    return ResponseEntity.ok(resgate);
  }
}