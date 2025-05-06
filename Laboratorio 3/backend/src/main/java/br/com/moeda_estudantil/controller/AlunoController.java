package br.com.moeda_estudantil.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.moeda_estudantil.model.Aluno;
import br.com.moeda_estudantil.service.AlunoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

  private final AlunoService alunoService;

  public AlunoController(AlunoService alunoService) {
    this.alunoService = alunoService;
  }

  @GetMapping
  @PreAuthorize("hasRole('PROFESSOR')")
  public ResponseEntity<List<Aluno>> listarTodos() {
    return ResponseEntity.ok(alunoService.listarTodos());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.isCurrentUser(#id)")
  public ResponseEntity<Aluno> buscarPorId(@PathVariable String id) {
    return ResponseEntity.ok(alunoService.buscarPorId(id));
  }

  @PostMapping("/cadastro")
  public ResponseEntity<Aluno> cadastrar(@Valid @RequestBody Aluno aluno) {
    Aluno alunoCriado = alunoService.cadastrar(aluno);
    return ResponseEntity.status(HttpStatus.CREATED).body(alunoCriado);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.isCurrentUser(#id)")
  public ResponseEntity<Aluno> atualizar(@PathVariable String id,
      @Valid @RequestBody Aluno aluno) {
    return ResponseEntity.ok(alunoService.atualizar(id, aluno));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('PROFESSOR')")
  public ResponseEntity<Void> deletar(@PathVariable String id) {
    alunoService.deletar(id);
    return ResponseEntity.noContent().build();
  }

  // Endpoint para adicionar moedas (usado pelos professores)
  @PostMapping("/{id}/adicionar-moedas")
  @PreAuthorize("hasRole('PROFESSOR')")
  public ResponseEntity<Aluno> adicionarMoedas(@PathVariable String id,
      @RequestParam Double quantidade) {
    return ResponseEntity.ok(alunoService.adicionarMoedas(id, quantidade));
  }

  // Endpoint para debitar moedas (usado pelo sistema ao resgatar vantagens)
  @PostMapping("/{id}/debitar-moedas")
  @PreAuthorize("hasRole('PROFESSOR') or @securityService.isCurrentUser(#id)")
  public ResponseEntity<Aluno> debitarMoedas(@PathVariable String id,
      @RequestParam Double quantidade) {
    return ResponseEntity.ok(alunoService.debitarMoedas(id, quantidade));
  }
}
