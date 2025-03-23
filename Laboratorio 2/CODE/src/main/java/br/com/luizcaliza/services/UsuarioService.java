package br.com.luizcaliza.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.repositories.UsuarioRepository;

public abstract class UsuarioService<T extends Usuario> {

  protected final UsuarioRepository<T> repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  protected UsuarioService(UsuarioRepository<T> repository) {
    this.repository = repository;
  }

  public List<T> findAll() {
    return repository.findAll();
  }

  public Page<T> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Optional<T> findById(Long id) {
    return repository.findById(id);
  }

  public Optional<T> findByUsername(String username) {
    return repository.findByUsername(username);
  }

  public T save(T usuario) {
    // Encode password before saving
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    return repository.save(usuario);
  }

  public T update(Long id, T usuario) {
    return repository.findById(id).map(existingUsuario -> {

      // Only update password if it's provided
      if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
        existingUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
      }

      return repository.save(existingUsuario);
    }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public boolean autenticar(String username, String senha) {
    Optional<T> usuario = repository.findByUsername(username);
    if (usuario.isPresent()) {
      return passwordEncoder.matches(senha, usuario.get().getPassword());
    }
    return false;
  }
}