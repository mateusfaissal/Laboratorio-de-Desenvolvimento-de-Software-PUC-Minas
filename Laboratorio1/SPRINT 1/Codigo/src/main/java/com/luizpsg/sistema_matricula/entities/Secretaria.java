package com.luizpsg.sistema_matricula.entities;

import jakarta.persistence.Entity;

@Entity
public class Secretaria extends User {

  public Secretaria() {
  }

  public Secretaria(String nome, String email, String senha) {
    super(nome, email, senha);
  }

  @Override
  public String toString() {
    return "Secretaria [id=" + getId() + ", nome=" + getNome() + ", email=" + getEmail() + "]";
  }
}
