package br.com.moeda_estudantil.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.moeda_estudantil.exception.RecursoJaExisteException;
import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.model.Aluno;
import br.com.moeda_estudantil.model.Curso;
import br.com.moeda_estudantil.model.Usuario;
import br.com.moeda_estudantil.repository.AlunoRepository;
import br.com.moeda_estudantil.repository.CursoRepository;

@Service
public class AlunoService {

  private final AlunoRepository alunoRepository;
  private final CursoRepository cursoRepository;
  private final PasswordEncoder passwordEncoder;

  public AlunoService(AlunoRepository alunoRepository, CursoRepository cursoRepository,
      PasswordEncoder passwordEncoder) {
    this.alunoRepository = alunoRepository;
    this.cursoRepository = cursoRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<Aluno> listarTodos() {
    return alunoRepository.findAll();
  }

  public Aluno buscarPorId(String id) {
    return alunoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));
  }

  @Transactional
  public Aluno cadastrar(Aluno aluno) {
    // Verificar se já existe aluno com o mesmo email, CPF ou RG
    if (alunoRepository.existsByEmail(aluno.getEmail())) {
      throw new RecursoJaExisteException("Email já cadastrado");
    }

    if (alunoRepository.existsByCpf(aluno.getCpf())) {
      throw new RecursoJaExisteException("CPF já cadastrado");
    }

    if (alunoRepository.existsByRg(aluno.getRg())) {
      throw new RecursoJaExisteException("RG já cadastrado");
    }

    // Verificar se o curso existe
    Curso curso = null;
    if (aluno.getCurso() != null && aluno.getCurso().getId() != null) {
      curso = cursoRepository.findById(aluno.getCurso().getId())
          .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado"));
      aluno.setCurso(curso);
    }

    // Garantir que o tipo de usuário é ALUNO
    aluno.setTipo(Usuario.TipoUsuario.ALUNO);

    // Inicializar o saldo de moedas
    aluno.setSaldoMoedas(0.0);

    // Criptografar a senha
    aluno.setSenha(passwordEncoder.encode(aluno.getSenha()));

    // Salvar aluno
    return alunoRepository.save(aluno);
  }

  @Transactional
  public Aluno atualizar(String id, Aluno alunoAtualizado) {
    Aluno alunoExistente = alunoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

    // Verificar se o email já está em uso por outro usuário
    if (!alunoExistente.getEmail().equals(alunoAtualizado.getEmail()) &&
        alunoRepository.existsByEmail(alunoAtualizado.getEmail())) {
      throw new RecursoJaExisteException("Email já cadastrado para outro usuário");
    }

    // Verificar se o CPF já está em uso por outro usuário
    if (!alunoExistente.getCpf().equals(alunoAtualizado.getCpf()) &&
        alunoRepository.existsByCpf(alunoAtualizado.getCpf())) {
      throw new RecursoJaExisteException("CPF já cadastrado para outro usuário");
    }

    // Verificar se o RG já está em uso por outro usuário
    if (!alunoExistente.getRg().equals(alunoAtualizado.getRg()) &&
        alunoRepository.existsByRg(alunoAtualizado.getRg())) {
      throw new RecursoJaExisteException("RG já cadastrado para outro usuário");
    }

    // Verificar se o curso existe
    Curso curso = null;
    if (alunoAtualizado.getCurso() != null && alunoAtualizado.getCurso().getId() != null) {
      curso = cursoRepository.findById(alunoAtualizado.getCurso().getId())
          .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado"));
    }

    // Atualizar dados do aluno
    alunoExistente.setNome(alunoAtualizado.getNome());
    alunoExistente.setEmail(alunoAtualizado.getEmail());
    alunoExistente.setCpf(alunoAtualizado.getCpf());
    alunoExistente.setRg(alunoAtualizado.getRg());
    alunoExistente.setEndereco(alunoAtualizado.getEndereco());

    if (curso != null) {
      alunoExistente.setCurso(curso);
    }

    // Só atualiza a senha se ela foi fornecida
    if (alunoAtualizado.getSenha() != null && !alunoAtualizado.getSenha().isEmpty()) {
      alunoExistente.setSenha(passwordEncoder.encode(alunoAtualizado.getSenha()));
    }

    // Não permitimos alteração direta do saldo de moedas
    // alunoExistente.setSaldoMoedas(alunoAtualizado.getSaldoMoedas());

    return alunoRepository.save(alunoExistente);
  }

  @Transactional
  public void deletar(String id) {
    if (!alunoRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Aluno não encontrado");
    }

    alunoRepository.deleteById(id);
  }

  @Transactional
  public Aluno adicionarMoedas(String id, Double quantidadeMoedas) {
    Aluno aluno = alunoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

    // Verificar se a quantidade é positiva
    if (quantidadeMoedas <= 0) {
      throw new IllegalArgumentException("A quantidade de moedas deve ser positiva");
    }

    // Adicionar moedas ao saldo atual
    double novoSaldo = aluno.getSaldoMoedas() + quantidadeMoedas;
    aluno.setSaldoMoedas(novoSaldo);

    return alunoRepository.save(aluno);
  }

  @Transactional
  public Aluno debitarMoedas(String id, Double quantidadeMoedas) {
    Aluno aluno = alunoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

    // Verificar se a quantidade é positiva
    if (quantidadeMoedas <= 0) {
      throw new IllegalArgumentException("A quantidade de moedas deve ser positiva");
    }

    // Verificar se o aluno possui saldo suficiente
    if (aluno.getSaldoMoedas() < quantidadeMoedas) {
      throw new IllegalArgumentException("Saldo de moedas insuficiente");
    }

    // Debitar moedas do saldo atual
    double novoSaldo = aluno.getSaldoMoedas() - quantidadeMoedas;
    aluno.setSaldoMoedas(novoSaldo);

    return alunoRepository.save(aluno);
  }
}