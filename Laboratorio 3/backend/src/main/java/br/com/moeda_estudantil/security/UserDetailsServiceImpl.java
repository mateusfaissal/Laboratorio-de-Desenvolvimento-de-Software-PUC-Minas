package br.com.moeda_estudantil.security;

import br.com.moeda_estudantil.model.Usuario;
import br.com.moeda_estudantil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

    List<GrantedAuthority> authorities = new ArrayList<>();

    // Adicionar autoridade baseada no tipo de usuário
    authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().name()));

    return new User(
        usuario.getEmail(),
        usuario.getSenha(),
        true, // enabled
        true, // accountNonExpired
        true, // credentialsNonExpired
        true, // accountNonLocked
        authorities);
  }
}