package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, String> {
  List<Curso> findByInstituicaoId(String instituicaoId);

  boolean existsByNomeAndInstituicaoId(String nome, String instituicaoId);

  @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
      "FROM Aluno a WHERE a.curso.id = :id")
  boolean hasAlunos(@Param("id") String id);
}