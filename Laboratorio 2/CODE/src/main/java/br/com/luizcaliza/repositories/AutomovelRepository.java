package br.com.luizcaliza.repositories;

import br.com.luizcaliza.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
  List<Automovel> findByProprietarioId(Long proprietarioId);

  List<Automovel> findByClienteId(Long clienteId);

  List<Automovel> findByBancoId(Long bancoId);

  Automovel findByPlaca(String placa);
}