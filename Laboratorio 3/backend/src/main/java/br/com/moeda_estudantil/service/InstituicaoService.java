package br.com.moeda_estudantil.service;

import br.com.moeda_estudantil.exception.RecursoJaExisteException;
import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.model.Instituicao;
import br.com.moeda_estudantil.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstituicaoService {

  @Autowired
  private InstituicaoRepository instituicaoRepository;

  public List<Instituicao> listarTodas() {
    return instituicaoRepository.findAll();
  }

  public Instituicao buscarPorId(String id) {
    return instituicaoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Instituição não encontrada"));
  }

  @Transactional
  public Instituicao criar(Instituicao instituicao) {
    // Verificar se já existe instituição com o mesmo nome
    if (instituicaoRepository.existsByNome(instituicao.getNome())) {
      throw new RecursoJaExisteException("Já existe uma instituição com esse nome");
    }

    return instituicaoRepository.save(instituicao);
  }

  @Transactional
  public Instituicao atualizar(String id, Instituicao instituicaoAtualizada) {
    Instituicao instituicaoExistente = instituicaoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Instituição não encontrada"));

    // Verificar se o nome já está em uso por outra instituição
    if (!instituicaoExistente.getNome().equals(instituicaoAtualizada.getNome()) &&
        instituicaoRepository.existsByNome(instituicaoAtualizada.getNome())) {
      throw new RecursoJaExisteException("Já existe uma instituição com esse nome");
    }

    // Atualizar dados da instituição
    instituicaoExistente.setNome(instituicaoAtualizada.getNome());
    instituicaoExistente.setEndereco(instituicaoAtualizada.getEndereco());

    return instituicaoRepository.save(instituicaoExistente);
  }

  @Transactional
  public void deletar(String id) {
    if (!instituicaoRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Instituição não encontrada");
    }

    // Verificar se a instituição possui cursos ou departamentos vinculados
    if (instituicaoRepository.hasRelatedEntities(id)) {
      throw new IllegalStateException(
          "Não é possível excluir a instituição porque ela possui cursos ou departamentos vinculados");
    }

    instituicaoRepository.deleteById(id);
  }
}