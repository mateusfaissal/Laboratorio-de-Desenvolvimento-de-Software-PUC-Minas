package br.com.luizcaliza.services;

import br.com.luizcaliza.model.Automovel;
import br.com.luizcaliza.model.ContratoDeCredito;
import br.com.luizcaliza.model.Pedido;
import br.com.luizcaliza.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

  @Autowired
  private PedidoRepository pedidoRepository;

  @Autowired
  @Lazy
  private AutomovelService automovelService;

  @Autowired
  private ContratoDeCreditoService contratoCreditoService;

  public Pedido salvar(Pedido pedido) {
    return pedidoRepository.save(pedido);
  }

  public Pedido atualizar(Pedido pedido) {
    return pedidoRepository.save(pedido);
  }

  public Pedido buscarPorId(Long id) {
    return pedidoRepository.findById(id).orElse(null);
  }

  public List<Pedido> listarTodos() {
    return pedidoRepository.findAll();
  }

  public List<Pedido> buscarPorClienteId(Long clienteId) {
    return pedidoRepository.findByClienteId(clienteId);
  }

  public List<Pedido> buscarPorStatus(String status) {
    return pedidoRepository.findByStatus(status);
  }

  public List<Pedido> buscarPorAutomovelId(Long automovelId) {
    return pedidoRepository.findByAutomovelId(automovelId);
  }

  public List<Pedido> buscarPorAutomovelBancoId(Long bancoId) {
    return pedidoRepository.findByAutomovelBancoId(bancoId);
  }

  public boolean associarAutomovel(Long pedidoId, Long automovelId) {
    Pedido pedido = buscarPorId(pedidoId);
    Automovel automovel = automovelService.buscarPorId(automovelId);

    if (pedido != null && automovel != null) {
      pedido.setAutomovel(automovel);
      pedidoRepository.save(pedido);
      return true;
    }
    return false;
  }

  public boolean associarContrato(Long pedidoId, Long contratoId) {
    Pedido pedido = buscarPorId(pedidoId);
    ContratoDeCredito contrato = contratoCreditoService.buscarPorId(contratoId);

    if (pedido != null && contrato != null) {
      pedido.setContrato(contrato);
      pedidoRepository.save(pedido);
      return true;
    }
    return false;
  }

  public boolean atualizarStatus(Long pedidoId, String novoStatus) {
    Pedido pedido = buscarPorId(pedidoId);

    if (pedido != null) {
      pedido.setStatus(novoStatus);
      pedidoRepository.save(pedido);
      return true;
    }
    return false;
  }

  public boolean definirProprietarioCarro(Long pedidoId, String proprietarioCarro) {
    Pedido pedido = buscarPorId(pedidoId);

    if (pedido != null) {
      pedido.setProprietarioCarro(proprietarioCarro);
      pedidoRepository.save(pedido);
      return true;
    }
    return false;
  }
}