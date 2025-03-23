package br.com.luizcaliza.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import br.com.luizcaliza.model.Usuario;

@NoRepositoryBean
public interface UsuarioRepository<T extends Usuario> extends JpaRepository<T, Long> {
  Optional<T> findByUsername(String username);
}