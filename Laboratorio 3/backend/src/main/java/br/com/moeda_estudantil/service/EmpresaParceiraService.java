package br.com.moeda_estudantil.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.moeda_estudantil.exception.RecursoJaExisteException;
import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.model.EmpresaParceira;
import br.com.moeda_estudantil.model.Usuario;
import br.com.moeda_estudantil.repository.EmpresaParceiraRepository;

@Service
public class EmpresaParceiraService {

  private final EmpresaParceiraRepository empresaRepository;
  private final PasswordEncoder passwordEncoder;

  public EmpresaParceiraService(EmpresaParceiraRepository empresaRepository,
      PasswordEncoder passwordEncoder) {
    this.empresaRepository = empresaRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<EmpresaParceira> listarTodas() {
    return empresaRepository.findAll();
  }

  public EmpresaParceira buscarPorId(String id) {
    return empresaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa parceira não encontrada"));
  }

  @Transactional
  public EmpresaParceira cadastrar(EmpresaParceira empresa) {
    // Verificar se já existe empresa com o mesmo email ou CNPJ
    if (empresaRepository.existsByEmail(empresa.getEmail())) {
      throw new RecursoJaExisteException("Email já cadastrado");
    }

    if (empresaRepository.existsByCnpj(empresa.getCnpj())) {
      throw new RecursoJaExisteException("CNPJ já cadastrado");
    }

    // Garantir que o tipo de usuário é EMPRESA
    empresa.setTipo(Usuario.TipoUsuario.EMPRESA);

    // Criptografar a senha
    empresa.setSenha(passwordEncoder.encode(empresa.getSenha()));

    // Salvar empresa
    return empresaRepository.save(empresa);
  }

  @Transactional
  public EmpresaParceira atualizar(String id, EmpresaParceira empresaAtualizada) {
    EmpresaParceira empresaExistente = empresaRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa parceira não encontrada"));

    // Verificar se o email já está em uso por outra empresa
    if (!empresaExistente.getEmail().equals(empresaAtualizada.getEmail()) &&
        empresaRepository.existsByEmail(empresaAtualizada.getEmail())) {
      throw new RecursoJaExisteException("Email já cadastrado para outra empresa");
    }

    // Verificar se o CNPJ já está em uso por outra empresa
    if (!empresaExistente.getCnpj().equals(empresaAtualizada.getCnpj()) &&
        empresaRepository.existsByCnpj(empresaAtualizada.getCnpj())) {
      throw new RecursoJaExisteException("CNPJ já cadastrado para outra empresa");
    }

    // Atualizar dados da empresa
    empresaExistente.setNome(empresaAtualizada.getNome());
    empresaExistente.setEmail(empresaAtualizada.getEmail());
    empresaExistente.setCpf(empresaAtualizada.getCpf());
    empresaExistente.setCnpj(empresaAtualizada.getCnpj());
    empresaExistente.setDescricao(empresaAtualizada.getDescricao());

    // Só atualiza a senha se ela foi fornecida
    if (empresaAtualizada.getSenha() != null && !empresaAtualizada.getSenha().isEmpty()) {
      empresaExistente.setSenha(passwordEncoder.encode(empresaAtualizada.getSenha()));
    }

    // Não atualizamos a lista de vantagens aqui, isso é feito pelo serviço de
    // vantagens

    return empresaRepository.save(empresaExistente);
  }

  @Transactional
  public void deletar(String id) {
    if (!empresaRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Empresa parceira não encontrada");
    }

    empresaRepository.deleteById(id);
  }
}
