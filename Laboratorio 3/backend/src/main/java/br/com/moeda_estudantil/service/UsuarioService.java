package br.com.moeda_estudantil.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.moeda_estudantil.exception.RecursoJaExisteException;
import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.exception.SenhaInvalidaException;
import br.com.moeda_estudantil.model.AlterarSenhaRequest;
import br.com.moeda_estudantil.model.Usuario;
import br.com.moeda_estudantil.repository.UsuarioRepository;

@Service
public class UsuarioService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<Usuario> listarTodos() {
    return usuarioRepository.findAll();
  }

  public Usuario buscarPorId(String id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
  }

  public Usuario buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
  }

  @Transactional
  public Usuario criar(Usuario usuario) {
    // Verificar se já existe usuário com o mesmo email ou CPF
    if (usuarioRepository.existsByEmail(usuario.getEmail())) {
      throw new RecursoJaExisteException("Email já cadastrado");
    }

    if (usuarioRepository.existsByCpf(usuario.getCpf())) {
      throw new RecursoJaExisteException("CPF já cadastrado");
    }

    // Criptografar a senha antes de salvar
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

    return usuarioRepository.save(usuario);
  }

  @Transactional
  public Usuario atualizar(String id, Usuario usuarioAtualizado) {
    Usuario usuarioExistente = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

    // Verificar se o email já está em uso por outro usuário
    if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail()) &&
        usuarioRepository.existsByEmail(usuarioAtualizado.getEmail())) {
      throw new RecursoJaExisteException("Email já cadastrado para outro usuário");
    }

    // Verificar se o CPF já está em uso por outro usuário
    if (!usuarioExistente.getCpf().equals(usuarioAtualizado.getCpf()) &&
        usuarioRepository.existsByCpf(usuarioAtualizado.getCpf())) {
      throw new RecursoJaExisteException("CPF já cadastrado para outro usuário");
    }

    // Atualizar dados do usuário
    usuarioExistente.setNome(usuarioAtualizado.getNome());
    usuarioExistente.setEmail(usuarioAtualizado.getEmail());
    usuarioExistente.setCpf(usuarioAtualizado.getCpf());

    // Só atualiza a senha se ela foi fornecida
    if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
      usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
    }

    // Não permitimos alteração do tipo de usuário
    // usuarioExistente.setTipo(usuarioAtualizado.getTipo());

    return usuarioRepository.save(usuarioExistente);
  }

  @Transactional
  public void deletar(String id) {
    if (!usuarioRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Usuário não encontrado");
    }

    usuarioRepository.deleteById(id);
  }

  @Transactional
  public void alterarSenha(String id, AlterarSenhaRequest request) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

    // Verificar se a senha atual está correta
    if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenha())) {
      throw new SenhaInvalidaException("Senha atual incorreta");
    }

    // Atualizar para a nova senha
    usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
    usuarioRepository.save(usuario);
  }

  // Método para autenticação (usado pelo serviço de autenticação)
  public boolean autenticar(String email, String senha) {
    return usuarioRepository.findByEmail(email)
        .map(u -> passwordEncoder.matches(senha, u.getSenha()))
        .orElse(false);
  }
}