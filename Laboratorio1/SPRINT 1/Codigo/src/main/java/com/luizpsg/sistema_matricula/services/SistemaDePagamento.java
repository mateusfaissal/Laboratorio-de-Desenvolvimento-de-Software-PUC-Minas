package com.luizpsg.sistema_matricula.services;

import com.luizpsg.sistema_matricula.entities.Aluno;

public interface SistemaDePagamento {
  void realizarPagamento(Aluno aluno, double valor);

}
