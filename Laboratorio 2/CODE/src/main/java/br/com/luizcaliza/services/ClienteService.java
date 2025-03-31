package br.com.luizcaliza.services;

import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.model.ContratoDeCredito;
import br.com.luizcaliza.model.Pedido;
import br.com.luizcaliza.model.Rendimento;
import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

  @Autowired
  private ClienteRepository clienteRepository;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  @Lazy
  private RendimentoService rendimentoService;

  @Autowired
  @Lazy
  private PedidoService pedidoService;

  public Cliente salvar(Cliente cliente, Usuario usuario) {
    usuario.setRole("CLIENTE");
    Usuario usuarioSalvo = usuarioService.salvar(usuario);

    cliente.setUsuario(usuarioSalvo);
    return clienteRepository.save(cliente);
  }

  public Cliente atualizar(Cliente cliente) {
    return clienteRepository.save(cliente);
  }

  public Cliente buscarPorId(Long id) {
    return clienteRepository.findById(id).orElse(null);
  }

  public Cliente buscarPorCpf(String cpf) {
    return clienteRepository.findByCpf(cpf);
  }

  public List<Cliente> listarTodos() {
    return clienteRepository.findAll();
  }

  public void adicionarRendimento(Cliente cliente, Rendimento rendimento) {
    rendimento.setCliente(cliente);
    rendimentoService.salvar(rendimento);

    cliente.getRendimentos().add(rendimento);
    clienteRepository.save(cliente);
  }

  public List<Rendimento> listarRendimentos(Long clienteId) {
    Cliente cliente = buscarPorId(clienteId);
    if (cliente != null) {
      return cliente.getRendimentos();
    }
    return List.of();
  }

  public boolean criarPedido(Cliente cliente, Pedido pedido) {
    pedido.setCliente(cliente);
    pedidoService.salvar(pedido);
    return true;
  }

  public List<Pedido> consultarPedidos(Long clienteId) {
    return pedidoService.buscarPorClienteId(clienteId);
  }

  public boolean modificarPedido(Pedido pedido) {
    if (pedido.getStatus().equals("NOVO")) {
      pedidoService.atualizar(pedido);
      return true;
    }
    return false;
  }

  public boolean cancelarPedido(Pedido pedido) {
    if (pedido.getStatus().equals("NOVO") || pedido.getStatus().equals("EM_ANALISE")) {
      pedido.setStatus("CANCELADO");
      pedidoService.atualizar(pedido);
      return true;
    }
    return false;
  }

  public boolean associarContrato(Pedido pedido, ContratoDeCredito contrato) {
    pedido.setContrato(contrato);
    pedidoService.atualizar(pedido);
    return true;
  }
}