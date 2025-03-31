package br.com.luizcaliza.services;

import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usuarioRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
  }

  public Usuario salvar(Usuario usuario) {
    usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    return usuarioRepository.save(usuario);
  }

  public Usuario buscarPorId(Long id) {
    return usuarioRepository.findById(id).orElse(null);
  }

  public Usuario buscarPorUsername(String username) {
    return usuarioRepository.findByUsername(username).orElse(null);
  }

  public List<Usuario> listarTodos() {
    return usuarioRepository.findAll();
  }

  public boolean autenticar(String username, String senha) {
    Usuario usuario = buscarPorUsername(username);
    if (usuario != null) {
      return passwordEncoder.matches(senha, usuario.getSenha());
    }
    return false;
  }
}