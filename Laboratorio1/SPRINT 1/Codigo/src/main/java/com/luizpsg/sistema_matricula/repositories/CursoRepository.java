package com.luizpsg.sistema_matricula.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizpsg.sistema_matricula.entities.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

}
