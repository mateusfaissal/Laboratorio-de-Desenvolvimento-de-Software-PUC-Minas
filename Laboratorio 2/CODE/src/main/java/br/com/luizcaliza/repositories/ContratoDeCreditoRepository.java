package br.com.luizcaliza.repositories;

import br.com.luizcaliza.model.ContratoDeCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoDeCreditoRepository extends JpaRepository<ContratoDeCredito, Long> {
  List<ContratoDeCredito> findByBancoId(Long bancoId);
}