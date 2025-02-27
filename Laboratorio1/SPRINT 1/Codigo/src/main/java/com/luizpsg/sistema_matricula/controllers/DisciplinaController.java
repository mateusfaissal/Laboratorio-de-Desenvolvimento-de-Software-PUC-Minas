package com.luizpsg.sistema_matricula.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luizpsg.sistema_matricula.entities.Disciplina;
import com.luizpsg.sistema_matricula.repositories.DisciplinaRepository;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

  @Autowired
  private DisciplinaRepository disciplinaRepository;

  @GetMapping
  public List<Disciplina> listarDisciplinas() {
    return disciplinaRepository.findAll();
  }

  @GetMapping("/total-alunos")
  public Integer totalAlunos(@RequestParam Long disciplinaId) {
    return disciplinaRepository.findById(disciplinaId).get().getAlunos().size();
  }

  @PutMapping

  @PostMapping
  public Disciplina cadastrarDisciplina(@RequestBody Disciplina disciplina) {
    return disciplinaRepository.save(disciplina);
  }

  @PostMapping("/lote")
  public List<Disciplina> cadastrarDisciplinas(@RequestBody List<Disciplina> disciplinas) {
    return disciplinaRepository.saveAll(disciplinas);
  }
}
