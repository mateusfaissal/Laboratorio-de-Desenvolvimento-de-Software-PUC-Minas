package br.com.luizcaliza.repositories;

import org.springframework.stereotype.Repository;

import br.com.luizcaliza.model.Cliente;

@Repository
public interface ClienteRepository extends UsuarioRepository<Cliente> {

}
