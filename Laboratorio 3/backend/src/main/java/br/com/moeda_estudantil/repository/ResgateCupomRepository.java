package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.ResgateCupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResgateCupomRepository extends JpaRepository<ResgateCupom, String> {
  List<ResgateCupom> findByAlunoId(String alunoId);

  @Query("SELECT rc FROM ResgateCupom rc WHERE rc.vantagem.empresa.id = :empresaId")
  List<ResgateCupom> findByVantagemEmpresaId(@Param("empresaId") String empresaId);
}