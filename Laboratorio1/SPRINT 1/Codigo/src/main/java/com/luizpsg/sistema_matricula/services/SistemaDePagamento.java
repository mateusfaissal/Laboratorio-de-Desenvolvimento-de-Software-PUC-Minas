package com.luizpsg.sistema_matricula.services;

import com.luizpsg.sistema_matricula.entities.Aluno;

public interface SistemaDePagamento {
  void enviarCobranca(Aluno aluno, double valor);

}
