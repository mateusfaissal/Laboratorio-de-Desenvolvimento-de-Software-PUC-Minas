package br.com.moeda_estudantil.service;

import br.com.moeda_estudantil.model.*;
import br.com.moeda_estudantil.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacaoService {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private NotificacaoRepository notificacaoRepository;

  @Async
  public void notificarRecebimentoMoedas(Aluno aluno, Professor professor, Double valor, String motivo) {
    // Criar a notificação no banco de dados
    Notificacao notificacao = new Notificacao();
    notificacao.setDestinatario(aluno.getEmail());
    notificacao.setAssunto("Você recebeu moedas no Sistema de Moeda Estudantil!");
    notificacao.setConteudo(
        "Olá " + aluno.getNome() + ",\n\n" +
            "Você acaba de receber " + valor + " moedas do professor " + professor.getNome() + ".\n\n" +
            "Motivo: " + motivo + "\n\n" +
            "Seu saldo atual é de " + aluno.getSaldoMoedas() + " moedas.\n\n" +
            "Acesse o sistema para ver as vantagens disponíveis para resgate.\n\n" +
            "Atenciosamente,\n" +
            "Equipe do Sistema de Moeda Estudantil");

    Notificacao notificacaoSalva = notificacaoRepository.save(notificacao);

    // Enviar email
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(notificacaoSalva.getDestinatario());
      message.setSubject(notificacaoSalva.getAssunto());
      message.setText(notificacaoSalva.getConteudo());

      mailSender.send(message);

      // Atualizar status da notificação
      notificacaoSalva.setStatus(Notificacao.StatusNotificacao.ENVIADO);
      notificacaoRepository.save(notificacaoSalva);
    } catch (Exception e) {
      // Log do erro, não interrompendo o fluxo principal
      System.err.println("Erro ao enviar notificação por email: " + e.getMessage());

      // Atualizar status da notificação
      notificacaoSalva.setStatus(Notificacao.StatusNotificacao.FALHA);
      notificacaoRepository.save(notificacaoSalva);
    }
  }

  @Async
  public void notificarResgateCupom(ResgateCupom resgate) {
    // Notificar o aluno
    notificarAlunoSobreResgate(resgate);

    // Notificar a empresa
    notificarEmpresaSobreResgate(resgate);
  }

  private void notificarAlunoSobreResgate(ResgateCupom resgate) {
    Aluno aluno = resgate.getAluno();
    Vantagem vantagem = resgate.getVantagem();

    // Criar a notificação no banco de dados
    Notificacao notificacao = new Notificacao();
    notificacao.setDestinatario(aluno.getEmail());
    notificacao.setAssunto("Seu cupom de resgate - " + vantagem.getNome());
    notificacao.setConteudo(
        "Olá " + aluno.getNome() + ",\n\n" +
            "Seu resgate da vantagem \"" + vantagem.getNome() + "\" foi realizado com sucesso!\n\n" +
            "Dados do resgate:\n" +
            "- Código do cupom: " + resgate.getCodigoGerado() + "\n" +
            "- Valor: " + resgate.getValor() + " moedas\n" +
            "- Empresa: " + vantagem.getEmpresa().getNome() + "\n\n" +
            "Para utilizar seu cupom, apresente o código acima na empresa parceira.\n\n" +
            "Seu saldo atual é de " + aluno.getSaldoMoedas() + " moedas.\n\n" +
            "Atenciosamente,\n" +
            "Equipe do Sistema de Moeda Estudantil");
    notificacao.setResgate(resgate);

    Notificacao notificacaoSalva = notificacaoRepository.save(notificacao);

    // Enviar email
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(notificacaoSalva.getDestinatario());
      message.setSubject(notificacaoSalva.getAssunto());
      message.setText(notificacaoSalva.getConteudo());

      mailSender.send(message);

      // Atualizar status da notificação
      notificacaoSalva.setStatus(Notificacao.StatusNotificacao.ENVIADO);
      notificacaoRepository.save(notificacaoSalva);
    } catch (Exception e) {
      System.err.println("Erro ao enviar notificação por email ao aluno: " + e.getMessage());

      // Atualizar status da notificação
      notificacaoSalva.setStatus(Notificacao.StatusNotificacao.FALHA);
      notificacaoRepository.save(notificacaoSalva);
    }
  }

  private void notificarEmpresaSobreResgate(ResgateCupom resgate) {
    Aluno aluno = resgate.getAluno();
    Vantagem vantagem = resgate.getVantagem();
    EmpresaParceira empresa = vantagem.getEmpresa();

    // Criar a notificação no banco de dados
    Notificacao notificacao = new Notificacao();
    notificacao.setDestinatario(empresa.getEmail());
    notificacao.setAssunto("Novo resgate de vantagem - " + vantagem.getNome());
    notificacao.setConteudo(
        "Olá " + empresa.getNome() + ",\n\n" +
            "Um aluno acabou de resgatar a vantagem \"" + vantagem.getNome() + "\".\n\n" +
            "Dados do resgate:\n" +
            "- Código do cupom: " + resgate.getCodigoGerado() + "\n" +
            "- Valor: " + resgate.getValor() + " moedas\n" +
            "- Aluno: " + aluno.getNome() + "\n" +
            "- Data do resgate: " + resgate.getDataResgate() + "\n\n" +
            "Quando o aluno apresentar este código para resgate da vantagem, utilize-o para validação no sistema.\n\n" +
            "Atenciosamente,\n" +
            "Equipe do Sistema de Moeda Estudantil");
    notificacao.setResgate(resgate);

    Notificacao notificacaoSalva = notificacaoRepository.save(notificacao);

    // Enviar email
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(notificacaoSalva.getDestinatario());
      message.setSubject(notificacaoSalva.getAssunto());
      message.setText(notificacaoSalva.getConteudo());

      mailSender.send(message);

      // Atualizar status da notificação
      notificacaoSalva.setStatus(Notificacao.StatusNotificacao.ENVIADO);
      notificacaoRepository.save(notificacaoSalva);
    } catch (Exception e) {
      System.err.println("Erro ao enviar notificação por email à empresa: " + e.getMessage());

      // Atualizar status da notificação
      notificacaoSalva.setStatus(Notificacao.StatusNotificacao.FALHA);
      notificacaoRepository.save(notificacaoSalva);
    }
  }

  // Job para reenviar notificações que falharam
  @Scheduled(fixedRate = 3600000) // A cada 1 hora
  @Transactional
  public void reenviarNotificacoesFalhadas() {
    List<Notificacao> notificacoesPendentes = notificacaoRepository.findByStatus(Notificacao.StatusNotificacao.FALHA);

    for (Notificacao notificacao : notificacoesPendentes) {
      try {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notificacao.getDestinatario());
        message.setSubject(notificacao.getAssunto());
        message.setText(notificacao.getConteudo());

        mailSender.send(message);

        // Atualizar status da notificação
        notificacao.setStatus(Notificacao.StatusNotificacao.ENVIADO);
        notificacaoRepository.save(notificacao);
      } catch (Exception e) {
        System.err.println("Erro ao reenviar notificação por email: " + e.getMessage());
        // Mantém o status como FALHA para tentar novamente na próxima execução
      }
    }
  }

  // Métodos para acesso às notificações

  public List<Notificacao> listarTodas() {
    return notificacaoRepository.findAll();
  }

  public List<Notificacao> listarPorDestinatario(String email) {
    return notificacaoRepository.findByDestinatario(email);
  }

  public List<Notificacao> listarPorStatus(Notificacao.StatusNotificacao status) {
    return notificacaoRepository.findByStatus(status);
  }

  public List<Notificacao> listarPorResgate(String resgateId) {
    return notificacaoRepository.findByResgateId(resgateId);
  }
}