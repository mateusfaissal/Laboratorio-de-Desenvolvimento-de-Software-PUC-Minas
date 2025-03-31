package br.com.luizcaliza.repositories;

import br.com.luizcaliza.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
  List<Pedido> findByClienteId(Long clienteId);

  List<Pedido> findByStatus(String status);

  List<Pedido> findByAutomovelId(Long automovelId);

  @Query("SELECT p FROM Pedido p WHERE p.automovel.banco.id = :bancoId")
  List<Pedido> findByAutomovelBancoId(@Param("bancoId") Long bancoId);
}