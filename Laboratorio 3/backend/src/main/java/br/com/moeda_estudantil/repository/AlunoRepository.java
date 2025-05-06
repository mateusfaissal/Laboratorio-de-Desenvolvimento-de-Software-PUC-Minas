package br.com.moeda_estudantil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.moeda_estudantil.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, String> {
  Optional<Aluno> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByCpf(String cpf);

  boolean existsByRg(String rg);
}
