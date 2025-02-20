package com.luizpsg.sistema_matricula.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luizpsg.sistema_matricula.entities.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

}
