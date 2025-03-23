package br.com.luizcaliza.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.services.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

  private final ClienteService clienteService;

  @Autowired
  public ClienteController(ClienteService clienteService) {
    this.clienteService = clienteService;
  }

  @GetMapping
  public ResponseEntity<List<Cliente>> getAllClientes() {
    return ResponseEntity.ok(clienteService.findAll());
  }

  @GetMapping("/paginado")
  public ResponseEntity<Page<Cliente>> getAllClientesPaginado(Pageable pageable) {
    return ResponseEntity.ok(clienteService.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
    return clienteService.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
    return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(cliente));
  }

  @PutMapping("/{id}/password")
  public ResponseEntity<Cliente> updatePassword(@PathVariable Long id, @RequestBody Cliente cliente) {
    return ResponseEntity.ok(clienteService.update(id, cliente));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
    return ResponseEntity.ok(clienteService.patch(id, cliente));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
    clienteService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/autenticar")
  public ResponseEntity<Boolean> autenticar(@RequestParam String username, @RequestParam String senha) {
    boolean autenticado = clienteService.autenticar(username, senha);
    return ResponseEntity.ok(autenticado);
  }
}