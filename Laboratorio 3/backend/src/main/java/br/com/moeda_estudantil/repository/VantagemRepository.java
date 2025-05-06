package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.Vantagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VantagemRepository extends JpaRepository<Vantagem, String> {
  List<Vantagem> findByEmpresaId(String empresaId);

  @Query("SELECT CASE WHEN COUNT(rc) > 0 THEN true ELSE false END " +
      "FROM ResgateCupom rc WHERE rc.vantagem.id = :id")
  boolean hasResgateCupons(@Param("id") String id);
}