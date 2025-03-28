package com.luizpsg.sistema_matricula.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Aluno extends User {

  @ManyToMany
  @JsonIgnore
  private List<Disciplina> disciplinas = new ArrayList<>();

  public Aluno() {
  }

  public Aluno(String nome, String email, String senha) {
    super(nome, email, senha);
  }

  public List<Disciplina> consultarDisciplinas() {
    return disciplinas;
  }

  public void addDisciplina(Disciplina disciplina) {
    if (!disciplinas.contains(disciplina)) {
      disciplina.addAluno(this);
      disciplinas.add(disciplina);
    }
  }

  public void removeDisciplina(Disciplina disciplina) {
    disciplinas.remove(disciplina);
  }

  @Override
  public String toString() {
    return "Aluno [disciplinas=" + disciplinas + ", getId()=" + getId() + ", getNome()=" + getNome() + ", getEmail()="
        + getEmail() + ", getSenha()=" + getSenha() + "]";
  }
}
