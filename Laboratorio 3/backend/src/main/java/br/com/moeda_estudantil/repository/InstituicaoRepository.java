package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.Instituicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, String> {
  boolean existsByNome(String nome);

  @Query("SELECT CASE WHEN COUNT(c) > 0 OR COUNT(d) > 0 THEN true ELSE false END " +
      "FROM Curso c, Departamento d " +
      "WHERE c.instituicao.id = :id OR d.instituicao.id = :id")
  boolean hasRelatedEntities(@Param("id") String id);
}