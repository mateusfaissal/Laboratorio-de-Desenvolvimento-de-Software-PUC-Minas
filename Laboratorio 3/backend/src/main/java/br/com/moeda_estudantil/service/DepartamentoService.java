package br.com.moeda_estudantil.service;

import br.com.moeda_estudantil.exception.RecursoJaExisteException;
import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.model.Departamento;
import br.com.moeda_estudantil.model.Instituicao;
import br.com.moeda_estudantil.repository.DepartamentoRepository;
import br.com.moeda_estudantil.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartamentoService {

  @Autowired
  private DepartamentoRepository departamentoRepository;

  @Autowired
  private InstituicaoRepository instituicaoRepository;

  public List<Departamento> listarTodos() {
    return departamentoRepository.findAll();
  }

  public List<Departamento> listarPorInstituicao(String instituicaoId) {
    return departamentoRepository.findByInstituicaoId(instituicaoId);
  }

  public Departamento buscarPorId(String id) {
    return departamentoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Departamento não encontrado"));
  }

  @Transactional
  public Departamento criar(Departamento departamento, String instituicaoId) {
    // Verificar se a instituição existe
    Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Instituição não encontrada"));

    // Verificar se já existe departamento com o mesmo nome na mesma instituição
    if (departamentoRepository.existsByNomeAndInstituicaoId(departamento.getNome(), instituicaoId)) {
      throw new RecursoJaExisteException(
          "Já existe um departamento com esse nome nesta instituição");
    }

    departamento.setInstituicao(instituicao);
    return departamentoRepository.save(departamento);
  }

  @Transactional
  public Departamento atualizar(String id, Departamento departamentoAtualizado) {
    Departamento departamentoExistente = departamentoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Departamento não encontrado"));

    // Verificar se o nome já está em uso por outro departamento na mesma
    // instituição
    String instituicaoId = departamentoExistente.getInstituicao().getId();
    if (!departamentoExistente.getNome().equals(departamentoAtualizado.getNome()) &&
        departamentoRepository.existsByNomeAndInstituicaoId(departamentoAtualizado.getNome(), instituicaoId)) {
      throw new RecursoJaExisteException(
          "Já existe um departamento com esse nome nesta instituição");
    }

    // Atualizar dados do departamento
    departamentoExistente.setNome(departamentoAtualizado.getNome());

    // Se uma nova instituição foi fornecida, verificar se ela existe
    if (departamentoAtualizado.getInstituicao() != null &&
        departamentoAtualizado.getInstituicao().getId() != null) {

      String novaInstituicaoId = departamentoAtualizado.getInstituicao().getId();

      // Verificar se é uma mudança real de instituição
      if (!instituicaoId.equals(novaInstituicaoId)) {
        // Verificar se a nova instituição existe
        Instituicao novaInstituicao = instituicaoRepository.findById(novaInstituicaoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Nova instituição não encontrada"));

        // Verificar se não há duplicidade na nova instituição
        if (departamentoRepository.existsByNomeAndInstituicaoId(
            departamentoAtualizado.getNome(), novaInstituicaoId)) {
          throw new RecursoJaExisteException(
              "Já existe um departamento com esse nome na instituição destino");
        }

        departamentoExistente.setInstituicao(novaInstituicao);
      }
    }

    return departamentoRepository.save(departamentoExistente);
  }

  @Transactional
  public void deletar(String id) {
    if (!departamentoRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Departamento não encontrado");
    }

    // Verificar se o departamento possui professores vinculados
    if (departamentoRepository.hasProfessores(id)) {
      throw new IllegalStateException(
          "Não é possível excluir o departamento porque ele possui professores vinculados");
    }

    departamentoRepository.deleteById(id);
  }
}