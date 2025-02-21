package com.luizpsg.sistema_matricula.entities;

import jakarta.persistence.Entity;

@Entity
public class Administrativo {
  public void verificarDisciplinasAtivas() {
    // Implementar lógica para verificar disciplinas ativas
  }

  public void gerarCurriculo(Aluno aluno) {
    // Implementar lógica para gerar currículo do aluno
  }

  public void manterCursos(Curso curso) {
    // Implementar lógica para manter cursos
  }

  public void manterDisciplinas(Curso curso, Disciplina disciplina) {
    // Implementar lógica para manter disciplinas de um curso
  }
}
