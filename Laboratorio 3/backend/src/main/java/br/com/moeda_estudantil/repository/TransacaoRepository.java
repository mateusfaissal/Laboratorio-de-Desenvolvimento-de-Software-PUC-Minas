package br.com.moeda_estudantil.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.moeda_estudantil.model.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, String> {

  @Query("SELECT t FROM Transacao t WHERE t.id IN " +
      "(SELECT ta.id FROM TransferenciaAluno ta WHERE ta.aluno.id = :alunoId) " +
      "OR t.id IN " +
      "(SELECT rc.id FROM ResgateCupom rc WHERE rc.aluno.id = :alunoId) " +
      "ORDER BY t.dataHora DESC")
  List<Transacao> findByAlunoId(@Param("alunoId") String alunoId);

  @Query("SELECT t FROM Transacao t WHERE t.id IN " +
      "(SELECT ta.id FROM TransferenciaAluno ta WHERE ta.professor.id = :professorId) " +
      "ORDER BY t.dataHora DESC")
  List<Transacao> findByProfessorId(@Param("professorId") String professorId);

  @Query("SELECT t FROM Transacao t WHERE t.id IN " +
      "(SELECT rc.id FROM ResgateCupom rc WHERE rc.vantagem.empresa.id = :empresaId) " +
      "ORDER BY t.dataHora DESC")
  List<Transacao> findByEmpresaId(@Param("empresaId") String empresaId);
}
