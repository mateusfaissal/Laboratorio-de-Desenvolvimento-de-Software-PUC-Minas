package com.luizpsg.sistema_matricula.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luizpsg.sistema_matricula.entities.Disciplina;
import com.luizpsg.sistema_matricula.entities.Professor;
import com.luizpsg.sistema_matricula.repositories.DisciplinaRepository;
import com.luizpsg.sistema_matricula.repositories.ProfessorRepository;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

  @Autowired
  private ProfessorRepository professorRepository;

  @Autowired
  private DisciplinaRepository disciplinaRepository;

  @GetMapping
  public List<Professor> listarProfessores() {
    return professorRepository.findAll();
  }

  @GetMapping("/buscar-professor")
  public Professor buscarProfessor(@RequestParam Long id) {
    return professorRepository.findById(id).get();
  }

  @PostMapping
  public Professor cadastrarProfessor(@RequestBody Professor professor) {
    return professorRepository.save(professor);
  }

  @PostMapping("/lote")
  public List<Professor> cadastrarProfessores(@RequestBody List<Professor> professores) {
    return professorRepository.saveAll(professores);
  }

  @PostMapping("/remover-disciplina")
  public Professor removerDisciplina(@RequestParam Long professorId, @RequestParam Long disciplinaId) {
    Professor professor = professorRepository.findById(professorId).get();
    Disciplina disciplina = disciplinaRepository.findById(disciplinaId).get();
    professor.removeDisciplina(disciplina);
    disciplina.setProfessor(null);
    disciplinaRepository.save(disciplina);
    return professorRepository.save(professor);
  }

  @PostMapping("/adicionar-disciplina")
  public Professor adicionarDisciplina(@RequestParam Long professorId, @RequestParam Long disciplinaId) {
    Professor professor = professorRepository.findById(professorId).get();
    Disciplina disciplina = disciplinaRepository.findById(disciplinaId).get();
    professor.addDisciplina(disciplina);
    disciplina.setProfessor(professor);
    disciplinaRepository.save(disciplina);
    return professorRepository.save(professor);
  }

}
