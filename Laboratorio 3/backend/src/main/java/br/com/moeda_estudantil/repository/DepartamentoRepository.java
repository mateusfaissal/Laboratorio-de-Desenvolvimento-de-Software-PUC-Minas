package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, String> {
  List<Departamento> findByInstituicaoId(String instituicaoId);

  boolean existsByNomeAndInstituicaoId(String nome, String instituicaoId);

  @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
      "FROM Professor p WHERE p.departamento.id = :id")
  boolean hasProfessores(@Param("id") String id);
}