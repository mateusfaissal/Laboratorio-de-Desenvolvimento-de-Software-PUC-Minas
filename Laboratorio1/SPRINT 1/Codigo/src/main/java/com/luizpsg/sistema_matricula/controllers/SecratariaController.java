package com.luizpsg.sistema_matricula.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizpsg.sistema_matricula.entities.Disciplina;
import com.luizpsg.sistema_matricula.entities.Secretaria;
import com.luizpsg.sistema_matricula.repositories.DisciplinaRepository;
import com.luizpsg.sistema_matricula.repositories.SecretariaRepository;

@RestController
@RequestMapping("/secretaria")
public class SecratariaController {

  @Autowired
  private SecretariaRepository secretariaRepository;

  @Autowired
  private DisciplinaRepository disciplinaRepository;

  @PostMapping
  public Secretaria cadastrarSecretaria(@RequestBody Secretaria secretaria) {
    return secretariaRepository.save(secretaria);
  }

  @PostMapping("/fechar")
  public void fecharPeriodoMatricula() {
    Secretaria secretaria = secretariaRepository.findAll().get(0);
    List<Disciplina> disciplinas = disciplinaRepository.findAll();
    List<Disciplina> disciplinasDesativadas = secretaria.fecharPeriodoMatricula(disciplinas);
    disciplinaRepository.saveAll(disciplinasDesativadas);
  }

}
