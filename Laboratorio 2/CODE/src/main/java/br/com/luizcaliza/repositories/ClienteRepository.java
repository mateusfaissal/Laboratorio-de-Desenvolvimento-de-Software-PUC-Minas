package br.com.luizcaliza.repositories;

import br.com.luizcaliza.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  Cliente findByCpf(String cpf);
}