package br.com.luizcaliza.services;

import org.springframework.stereotype.Service;

import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.repositories.ClienteRepository;

@Service
public class ClienteService extends UsuarioService<Cliente> {

  public ClienteService(ClienteRepository repository) {
    super(repository);
  }

  // @Override
  // public Cliente update(Long id, Cliente cliente) {
  // return repository.findById(id).map(existingCliente -> {

  // if (cliente.getPassword() != null && !cliente.getPassword().isEmpty()) {
  // existingCliente.setPassword(cliente.getPassword());
  // }

  // existingCliente.setNome(cliente.getNome());
  // existingCliente.setRg(cliente.getRg());
  // existingCliente.setCpf(cliente.getCpf());
  // existingCliente.setProfissao(cliente.getProfissao());
  // existingCliente.setEndereco(cliente.getEndereco());
  // existingCliente.setRendimentos(cliente.getRendimentos());

  // return repository.save(existingCliente);
  // }).orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " +
  // id));
  // }

  public Cliente patch(Long id, Cliente cliente) {
    return repository.findById(id).map(existingCliente -> {

      if (cliente.getNome() != null && !cliente.getNome().isEmpty()) {
        existingCliente.setNome(cliente.getNome());
      }

      if (cliente.getRg() != null && !cliente.getRg().isEmpty()) {
        existingCliente.setRg(cliente.getRg());
      }

      if (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) {
        existingCliente.setCpf(cliente.getCpf());
      }

      if (cliente.getProfissao() != null && !cliente.getProfissao().isEmpty()) {
        existingCliente.setProfissao(cliente.getProfissao());
      }

      if (cliente.getEndereco() != null) {
        existingCliente.setEndereco(cliente.getEndereco());
      }

      if (cliente.getRendimentos() != null) {
        existingCliente.setRendimentos(cliente.getRendimentos());
      }

      return repository.save(existingCliente);
    }).orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
  }
}