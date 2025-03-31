package br.com.luizcaliza.services;

import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.model.Rendimento;
import br.com.luizcaliza.repositories.RendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RendimentoService {

  @Autowired
  private RendimentoRepository rendimentoRepository;

  @Autowired
  @Lazy
  private ClienteService clienteService;

  public Rendimento salvar(Rendimento rendimento) {
    return rendimentoRepository.save(rendimento);
  }

  public Rendimento atualizar(Rendimento rendimento) {
    return rendimentoRepository.save(rendimento);
  }

  public Rendimento buscarPorId(Long id) {
    return rendimentoRepository.findById(id).orElse(null);
  }

  public List<Rendimento> listarTodos() {
    return rendimentoRepository.findAll();
  }

  public List<Rendimento> buscarPorClienteId(Long clienteId) {
    return rendimentoRepository.findByClienteId(clienteId);
  }

  public boolean associarCliente(Long rendimentoId, Long clienteId) {
    Rendimento rendimento = buscarPorId(rendimentoId);
    Cliente cliente = clienteService.buscarPorId(clienteId);

    if (rendimento != null && cliente != null) {
      rendimento.setCliente(cliente);
      rendimentoRepository.save(rendimento);
      return true;
    }
    return false;
  }
}