package com.luizpsg.sistema_matricula.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luizpsg.sistema_matricula.entities.Aluno;
import com.luizpsg.sistema_matricula.entities.Disciplina;
import com.luizpsg.sistema_matricula.repositories.AlunoRepository;
import com.luizpsg.sistema_matricula.repositories.DisciplinaRepository;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private DisciplinaRepository disciplinaRepository;

  @GetMapping
  public List<Aluno> listarAlunos() {
    return alunoRepository.findAll();
  }

  @PostMapping
  public Aluno cadastrarAluno(@RequestBody Aluno aluno) {
    return alunoRepository.save(aluno);
  }

  @PostMapping("/matricular")
  public Aluno matricular(@RequestParam Long alunoId, @RequestParam Long disciplinaId) {
    Aluno aluno = alunoRepository.findById(alunoId).get();
    Disciplina disciplina = disciplinaRepository.findById(disciplinaId).get();
    aluno.addDisciplina(disciplina);
    return alunoRepository.save(aluno);
  }

}
