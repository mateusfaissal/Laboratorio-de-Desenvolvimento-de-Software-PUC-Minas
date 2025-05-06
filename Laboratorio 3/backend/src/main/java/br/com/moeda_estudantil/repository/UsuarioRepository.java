package br.com.moeda_estudantil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.moeda_estudantil.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
  Optional<Usuario> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByCpf(String cpf);
}
