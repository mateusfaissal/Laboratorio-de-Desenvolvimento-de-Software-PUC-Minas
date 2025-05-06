package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.TransferenciaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaAlunoRepository extends JpaRepository<TransferenciaAluno, String> {
  List<TransferenciaAluno> findByAlunoId(String alunoId);

  List<TransferenciaAluno> findByProfessorId(String professorId);
}
