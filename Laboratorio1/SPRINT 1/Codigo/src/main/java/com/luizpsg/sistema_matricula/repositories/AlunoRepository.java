package com.luizpsg.sistema_matricula.repositories;

import com.luizpsg.sistema_matricula.entities.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

}
