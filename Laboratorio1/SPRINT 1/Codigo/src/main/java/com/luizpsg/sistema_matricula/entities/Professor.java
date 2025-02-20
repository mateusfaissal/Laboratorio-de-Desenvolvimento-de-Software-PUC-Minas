package com.luizpsg.sistema_matricula.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Professor extends User {

  @OneToMany(mappedBy = "professor")
  private List<Disciplina> disciplinasLecionadas = new ArrayList<>();

  public Professor() {
  }

  public Professor(String nome, String email, String senha) {
    super(nome, email, senha);
  }

  public List<Disciplina> getDisciplinasLecionadas() {
    return disciplinasLecionadas;
  }

  public void addDisciplina(Disciplina disciplina) {
    disciplinasLecionadas.add(disciplina);
  }

  public void removeDisciplina(Disciplina disciplina) {
    disciplinasLecionadas.remove(disciplina);
  }

  @Override
  public String toString() {
    return "Professor [disciplinasLecionadas=" + disciplinasLecionadas + "]";
  }
}
