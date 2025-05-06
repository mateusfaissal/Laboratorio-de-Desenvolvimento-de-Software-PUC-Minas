package br.com.moeda_estudantil.service;

import br.com.moeda_estudantil.exception.RecursoJaExisteException;
import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.model.Curso;
import br.com.moeda_estudantil.model.Instituicao;
import br.com.moeda_estudantil.repository.CursoRepository;
import br.com.moeda_estudantil.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoService {

  @Autowired
  private CursoRepository cursoRepository;

  @Autowired
  private InstituicaoRepository instituicaoRepository;

  public List<Curso> listarTodos() {
    return cursoRepository.findAll();
  }

  public List<Curso> listarPorInstituicao(String instituicaoId) {
    return cursoRepository.findByInstituicaoId(instituicaoId);
  }

  public Curso buscarPorId(String id) {
    return cursoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado"));
  }

  @Transactional
  public Curso criar(Curso curso, String instituicaoId) {
    // Verificar se a instituição existe
    Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Instituição não encontrada"));

    // Verificar se já existe curso com o mesmo nome na mesma instituição
    if (cursoRepository.existsByNomeAndInstituicaoId(curso.getNome(), instituicaoId)) {
      throw new RecursoJaExisteException(
          "Já existe um curso com esse nome nesta instituição");
    }

    curso.setInstituicao(instituicao);
    return cursoRepository.save(curso);
  }

  @Transactional
  public Curso atualizar(String id, Curso cursoAtualizado) {
    Curso cursoExistente = cursoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Curso não encontrado"));

    // Verificar se o nome já está em uso por outro curso na mesma instituição
    String instituicaoId = cursoExistente.getInstituicao().getId();
    if (!cursoExistente.getNome().equals(cursoAtualizado.getNome()) &&
        cursoRepository.existsByNomeAndInstituicaoId(cursoAtualizado.getNome(), instituicaoId)) {
      throw new RecursoJaExisteException(
          "Já existe um curso com esse nome nesta instituição");
    }

    // Atualizar dados do curso
    cursoExistente.setNome(cursoAtualizado.getNome());

    // Se uma nova instituição foi fornecida, verificar se ela existe
    if (cursoAtualizado.getInstituicao() != null &&
        cursoAtualizado.getInstituicao().getId() != null) {

      String novaInstituicaoId = cursoAtualizado.getInstituicao().getId();

      // Verificar se é uma mudança real de instituição
      if (!instituicaoId.equals(novaInstituicaoId)) {
        // Verificar se a nova instituição existe
        Instituicao novaInstituicao = instituicaoRepository.findById(novaInstituicaoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Nova instituição não encontrada"));

        // Verificar se não há duplicidade na nova instituição
        if (cursoRepository.existsByNomeAndInstituicaoId(
            cursoAtualizado.getNome(), novaInstituicaoId)) {
          throw new RecursoJaExisteException(
              "Já existe um curso com esse nome na instituição destino");
        }

        cursoExistente.setInstituicao(novaInstituicao);
      }
    }

    return cursoRepository.save(cursoExistente);
  }

  @Transactional
  public void deletar(String id) {
    if (!cursoRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Curso não encontrado");
    }

    // Verificar se o curso possui alunos vinculados
    if (cursoRepository.hasAlunos(id)) {
      throw new IllegalStateException(
          "Não é possível excluir o curso porque ele possui alunos vinculados");
    }

    cursoRepository.deleteById(id);
  }
}
