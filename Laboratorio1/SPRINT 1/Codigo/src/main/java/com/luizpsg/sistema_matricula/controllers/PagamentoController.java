package com.luizpsg.sistema_matricula.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luizpsg.sistema_matricula.entities.Aluno;
import com.luizpsg.sistema_matricula.repositories.AlunoRepository;
import com.luizpsg.sistema_matricula.services.SistemaDePagamentoImpl;

@RestController
@RequestMapping("/pagamento")
public class PagamentoController {

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private SistemaDePagamentoImpl sistemaDePagamento;

  @PostMapping("/realizar")
  public String enviarCobranca(@RequestParam Long alunoId, @RequestParam double valor) {
    Aluno aluno = alunoRepository.findById(alunoId)
        .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

    sistemaDePagamento.enviarCobranca(aluno, valor);
    return "Pagamento realizado e e-mail enviado para o aluno " + aluno.getNome();
  }

}
