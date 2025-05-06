package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, String> {
  Optional<Professor> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByCpf(String cpf);

  List<Professor> findByDepartamentoId(String departamentoId);
}