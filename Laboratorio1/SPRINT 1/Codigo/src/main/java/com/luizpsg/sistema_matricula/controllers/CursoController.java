package com.luizpsg.sistema_matricula.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luizpsg.sistema_matricula.entities.Curso;
import com.luizpsg.sistema_matricula.entities.Disciplina;
import com.luizpsg.sistema_matricula.repositories.CursoRepository;
import com.luizpsg.sistema_matricula.repositories.DisciplinaRepository;

@RestController
@RequestMapping("/curso")
public class CursoController {

  @Autowired
  private CursoRepository cursoRepository;

  @Autowired
  private DisciplinaRepository disciplinaRepository;

  @GetMapping
  public List<Curso> listarCursos() {
    return cursoRepository.findAll();
  }

  @PostMapping
  public Curso cadastrarCurso(@RequestBody Curso curso) {
    return cursoRepository.save(curso);
  }

  @PostMapping("/lote")
  public List<Curso> cadastrarCursos(@RequestBody List<Curso> cursos) {
    return cursoRepository.saveAll(cursos);
  }

  @PostMapping("/add-disciplina")
  public Curso addDisciplina(@RequestParam Long cursoId, @RequestParam Long disciplinaId) {
    Curso curso = cursoRepository.findById(cursoId).get();
    Disciplina disciplina = disciplinaRepository.findById(disciplinaId).get();
    curso.addDisciplina(disciplina);
    return cursoRepository.save(curso);
  }
}
