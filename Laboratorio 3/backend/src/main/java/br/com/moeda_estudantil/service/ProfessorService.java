package br.com.moeda_estudantil.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.moeda_estudantil.exception.RecursoJaExisteException;
import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.model.Aluno;
import br.com.moeda_estudantil.model.Professor;
import br.com.moeda_estudantil.model.Transacao.TipoTransacao;
import br.com.moeda_estudantil.model.TransferenciaAluno;
import br.com.moeda_estudantil.repository.AlunoRepository;
import br.com.moeda_estudantil.repository.ProfessorRepository;
import br.com.moeda_estudantil.repository.TransferenciaAlunoRepository;

@Service
public class ProfessorService {

  @Autowired
  private ProfessorRepository professorRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private TransferenciaAlunoRepository transferenciaRepository;

  @Autowired
  private NotificacaoService notificacaoService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public List<Professor> listarTodos() {
    return professorRepository.findAll();
  }

  public List<Professor> listarPorDepartamento(String departamentoId) {
    return professorRepository.findByDepartamentoId(departamentoId);
  }

  public Professor buscarPorId(String id) {
    return professorRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));
  }

  public Professor buscarPorEmail(String email) {
    return professorRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));
  }

  @Transactional
  public Professor atualizar(String id, Professor professorAtualizado) {
    Professor professorExistente = professorRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));

    // Verificar se o email já está em uso por outro usuário
    if (!professorExistente.getEmail().equals(professorAtualizado.getEmail()) &&
        professorRepository.existsByEmail(professorAtualizado.getEmail())) {
      throw new RecursoJaExisteException("Email já cadastrado para outro usuário");
    }

    // Verificar se o CPF já está em uso por outro usuário
    if (!professorExistente.getCpf().equals(professorAtualizado.getCpf()) &&
        professorRepository.existsByCpf(professorAtualizado.getCpf())) {
      throw new RecursoJaExisteException("CPF já cadastrado para outro usuário");
    }

    // Atualizar dados do professor
    professorExistente.setNome(professorAtualizado.getNome());
    professorExistente.setEmail(professorAtualizado.getEmail());
    professorExistente.setCpf(professorAtualizado.getCpf());

    // Só atualiza a senha se ela foi fornecida
    if (professorAtualizado.getSenha() != null && !professorAtualizado.getSenha().isEmpty()) {
      professorExistente.setSenha(passwordEncoder.encode(professorAtualizado.getSenha()));
    }

    // Atualizar departamento se fornecido
    if (professorAtualizado.getDepartamento() != null) {
      professorExistente.setDepartamento(professorAtualizado.getDepartamento());
    }

    // Não permitimos alteração direta do saldo de moedas
    // professorExistente.setSaldoMoedas(professorAtualizado.getSaldoMoedas());

    return professorRepository.save(professorExistente);
  }

  public Double consultarSaldo(String email) {
    Professor professor = professorRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));

    return professor.getSaldoMoedas();
  }

  @Transactional
  public TransferenciaAluno enviarMoedas(String professorEmail, String alunoId, Double valor, String motivo) {
    // Buscar professor
    Professor professor = professorRepository.findByEmail(professorEmail)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));

    // Verificar se o saldo é suficiente
    if (professor.getSaldoMoedas() < valor) {
      throw new IllegalArgumentException("Saldo de moedas insuficiente");
    }

    // Buscar aluno
    Aluno aluno = alunoRepository.findById(alunoId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

    // Criar transferência
    TransferenciaAluno transferencia = new TransferenciaAluno();
    transferencia.setProfessor(professor);
    transferencia.setAluno(aluno);
    transferencia.setValor(valor);
    transferencia.setMotivoReconhecimento(motivo);
    transferencia.setTipo(TipoTransacao.TRANSFERENCIA);
    transferencia.setDescricao("Transferência de " + professor.getNome() + " para " + aluno.getNome());

    // Atualizar saldos
    professor.setSaldoMoedas(professor.getSaldoMoedas() - valor);
    aluno.setSaldoMoedas(aluno.getSaldoMoedas() + valor);

    // Salvar alterações
    professorRepository.save(professor);
    alunoRepository.save(aluno);
    TransferenciaAluno transferenciaSalva = transferenciaRepository.save(transferencia);

    // Enviar notificação ao aluno
    notificacaoService.notificarRecebimentoMoedas(aluno, professor, valor, motivo);

    return transferenciaSalva;
  }

  public List<TransferenciaAluno> consultarExtrato(String email) {
    Professor professor = professorRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Professor não encontrado"));

    return transferenciaRepository.findByProfessorId(professor.getId());
  }

  // Método para adicionar moedas semestralmente aos professores
  @Scheduled(cron = "0 0 0 1 1,7 *") // Executa às 00:00 de 1º de janeiro e 1º de julho
  @Transactional
  public void adicionarMoedasSemestrais() {
    final double MOEDAS_POR_SEMESTRE = 1000.0;

    List<Professor> professores = professorRepository.findAll();

    for (Professor professor : professores) {
      professor.setSaldoMoedas(professor.getSaldoMoedas() + MOEDAS_POR_SEMESTRE);
      professorRepository.save(professor);

      // Registrar a transação no log
      System.out.println("Adicionadas " + MOEDAS_POR_SEMESTRE + " moedas ao professor " +
          professor.getNome() + " (" + professor.getId() + ") - " +
          "Novo saldo: " + professor.getSaldoMoedas() + " - " +
          "Data: " + LocalDateTime.now());
    }
  }
}