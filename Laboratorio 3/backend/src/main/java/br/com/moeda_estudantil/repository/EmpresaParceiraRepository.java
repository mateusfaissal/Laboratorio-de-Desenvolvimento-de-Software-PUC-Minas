package br.com.moeda_estudantil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.moeda_estudantil.model.EmpresaParceira;

@Repository
public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, String> {
  Optional<EmpresaParceira> findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByCnpj(String cnpj);
}