package br.com.moeda_estudantil.service;

import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.exception.SenhaInvalidaException;
import br.com.moeda_estudantil.model.*;
import br.com.moeda_estudantil.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransacaoService {

  @Autowired
  private TransacaoRepository transacaoRepository;

  @Autowired
  private TransferenciaAlunoRepository transferenciaRepository;

  @Autowired
  private ResgateCupomRepository resgateRepository;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private ProfessorRepository professorRepository;

  @Autowired
  private EmpresaParceiraRepository empresaRepository;

  @Autowired
  private NotificacaoService notificacaoService;

  public List<Transacao> buscarTransacoesPorUsuario(String email) {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

    if (usuario.isAluno()) {
      return transacaoRepository.findByAlunoId(usuario.getId());
    } else if (usuario.isProfessor()) {
      return transacaoRepository.findByProfessorId(usuario.getId());
    } else if (usuario.isEmpresa()) {
      return transacaoRepository.findByEmpresaId(usuario.getId());
    }

    return List.of();
  }

  public List<TransferenciaAluno> buscarTransferenciasParaAluno(String alunoId) {
    return transferenciaRepository.findByAlunoId(alunoId);
  }

  public List<TransferenciaAluno> buscarTransferenciasDoProfessor(String professorId) {
    return transferenciaRepository.findByProfessorId(professorId);
  }

  public List<ResgateCupom> buscarCuponsPorAluno(String email) {
    Aluno aluno = alunoRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

    return resgateRepository.findByAlunoId(aluno.getId());
  }

  public List<ResgateCupom> buscarCuponsPorEmpresa(String empresaId) {
    // Busca cupons onde a vantagem pertence à empresa especificada
    return resgateRepository.findByVantagemEmpresaId(empresaId);
  }

  public ResgateCupom buscarCupomPorId(String cupomId, String email) {
    ResgateCupom cupom = resgateRepository.findById(cupomId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Cupom não encontrado"));

    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

    // Verifica se o usuário tem permissão para acessar o cupom
    if (usuario.isAluno() && !cupom.getAluno().getId().equals(usuario.getId())) {
      throw new SenhaInvalidaException("Você não tem permissão para acessar este cupom");
    } else if (usuario.isEmpresa() && !cupom.getVantagem().getEmpresa().getId().equals(usuario.getId())) {
      throw new SenhaInvalidaException("Você não tem permissão para acessar este cupom");
    }

    return cupom;
  }

  @Transactional
  public ResgateCupom validarCupom(String cupomId, String codigoConfirmacao, String email) {
    ResgateCupom cupom = resgateRepository.findById(cupomId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Cupom não encontrado"));

    // Verifica se o cupom já foi utilizado
    if (cupom.getUtilizado()) {
      throw new IllegalArgumentException("Cupom já utilizado");
    }

    // Verifica se o código de confirmação está correto
    if (!cupom.getCodigoGerado().equals(codigoConfirmacao)) {
      throw new IllegalArgumentException("Código de confirmação inválido");
    }

    // Verifica se a empresa que está validando é a dona da vantagem
    EmpresaParceira empresa = empresaRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa não encontrada"));

    if (!cupom.getVantagem().getEmpresa().getId().equals(empresa.getId())) {
      throw new IllegalArgumentException("Esta vantagem não pertence à sua empresa");
    }

    // Marca o cupom como utilizado
    cupom.setUtilizado(true);
    cupom.setDataUtilizacao(LocalDateTime.now());

    return resgateRepository.save(cupom);
  }
}