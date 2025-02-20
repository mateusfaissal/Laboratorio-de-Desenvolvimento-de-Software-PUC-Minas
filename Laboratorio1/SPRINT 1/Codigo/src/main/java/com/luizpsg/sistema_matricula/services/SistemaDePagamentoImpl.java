package com.luizpsg.sistema_matricula.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizpsg.sistema_matricula.entities.Aluno;

@Service
public class SistemaDePagamentoImpl implements SistemaDePagamento {

  @Autowired
  private EmailService emailService;

  @Override
  public void realizarPagamento(Aluno aluno, double valor) {
    // Lógica do pagamento (exemplo simples)
    // Pode integrar com APIs de pagamento, verificar saldo, etc.
    System.out.println("boleto de R$" + valor + " enviado para o aluno: " + aluno.getNome());

    // Ao final, enviamos um e-mail de notificação
    String assunto = "Boleto de Matrícula";
    String mensagem = "Olá " + aluno.getNome() + ", seu boleto de R$" + valor + " foi gerado!";

    emailService.enviarEmail(aluno.getEmail(), assunto, mensagem);
  }
}
