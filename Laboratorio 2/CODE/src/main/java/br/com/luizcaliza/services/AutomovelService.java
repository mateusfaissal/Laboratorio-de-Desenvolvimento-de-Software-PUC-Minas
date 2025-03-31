package br.com.luizcaliza.services;

import br.com.luizcaliza.model.Automovel;
import br.com.luizcaliza.model.Banco;
import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.model.Pedido;
import br.com.luizcaliza.repositories.AutomovelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AutomovelService {

  @Autowired
  private AutomovelRepository automovelRepository;

  @Autowired
  @Lazy
  private ClienteService clienteService;

  @Autowired
  private BancoService bancoService;

  @Autowired
  @Lazy
  private PedidoService pedidoService;

  public Automovel salvar(Automovel automovel) {
    return automovelRepository.save(automovel);
  }

  public Automovel atualizar(Automovel automovel) {
    return automovelRepository.save(automovel);
  }

  public Automovel buscarPorId(Long id) {
    return automovelRepository.findById(id).orElse(null);
  }

  public Automovel buscarPorPlaca(String placa) {
    return automovelRepository.findByPlaca(placa);
  }

  public List<Automovel> listarTodos() {
    return automovelRepository.findAll();
  }

  public List<Automovel> listarPorProprietario(Long proprietarioId) {
    return automovelRepository.findByProprietarioId(proprietarioId);
  }

  public List<Automovel> listarPorCliente(Long clienteId) {
    return automovelRepository.findByClienteId(clienteId);
  }

  public List<Automovel> listarPorBanco(Long bancoId) {
    return automovelRepository.findByBancoId(bancoId);
  }

  /**
   * Verifica se um automóvel está disponível para o período solicitado.
   * Um automóvel só estará indisponível se tiver pedidos APROVADOS com datas que
   * se sobreponham.
   * 
   * @param automovelId ID do automóvel a ser verificado
   * @param dataInicio  Data de início do período
   * @param dataFim     Data de fim do período
   * @return true se o automóvel estiver disponível, false caso contrário
   */
  public boolean verificarDisponibilidade(Long automovelId, Date dataInicio, Date dataFim) {
    if (dataInicio == null || dataFim == null) {
      return false;
    }

    // Obter o automóvel e seus pedidos
    Automovel automovel = buscarPorId(automovelId);
    if (automovel == null) {
      return false;
    }

    List<Pedido> pedidos = pedidoService.buscarPorAutomovelId(automovelId);

    // Verificar se há pedidos aprovados que se sobreponham ao período solicitado
    for (Pedido pedido : pedidos) {
      // Só considerar pedidos APROVADOS
      if ("APROVADO".equals(pedido.getStatus())) {
        // Verificar se há sobreposição de datas
        if (pedido.getDataInicio() != null && pedido.getDataFim() != null) {
          // Verifica se há sobreposição:
          // (dataInicio <= pedido.dataFim) && (dataFim >= pedido.dataInicio)
          if (dataInicio.compareTo(pedido.getDataFim()) <= 0 &&
              dataFim.compareTo(pedido.getDataInicio()) >= 0) {
            return false; // Há sobreposição, automóvel não disponível
          }
        }
      }
    }

    return true; // Não há sobreposição, automóvel disponível
  }

  /**
   * Lista todos os automóveis disponíveis para um determinado período.
   * 
   * @param dataInicio Data de início do período
   * @param dataFim    Data de fim do período
   * @return Lista de automóveis disponíveis
   */
  public List<Automovel> listarDisponiveisPorPeriodo(Date dataInicio, Date dataFim) {
    List<Automovel> todosAutomoveis = listarTodos();
    return todosAutomoveis.stream()
        .filter(auto -> verificarDisponibilidade(auto.getId(), dataInicio, dataFim))
        .toList();
  }

  public boolean definirProprietarioCliente(Long automovelId, Long clienteId) {
    Automovel automovel = buscarPorId(automovelId);
    Cliente cliente = clienteService.buscarPorId(clienteId);

    if (automovel != null && cliente != null) {
      automovel.setProprietario(cliente.getUsuario());
      automovel.setCliente(cliente);
      automovel.setBanco(null);
      automovelRepository.save(automovel);
      return true;
    }
    return false;
  }

  public boolean definirProprietarioBanco(Long automovelId, Long bancoId) {
    Automovel automovel = buscarPorId(automovelId);
    Banco banco = bancoService.buscarPorId(bancoId);

    if (automovel != null && banco != null) {
      automovel.setProprietario(banco.getUsuario());
      automovel.setCliente(null);
      automovel.setBanco(banco);
      automovelRepository.save(automovel);
      return true;
    }
    return false;
  }

  public void atualizarDados(Long automovelId, String matricula, Integer ano, String marca, String modelo,
      String placa, Double precoDiaria) {
    Automovel automovel = buscarPorId(automovelId);

    if (automovel != null) {
      automovel.setMatricula(matricula);
      automovel.setAno(ano);
      automovel.setMarca(marca);
      automovel.setModelo(modelo);
      automovel.setPlaca(placa);
      automovel.setPrecoDiaria(precoDiaria);
      automovelRepository.save(automovel);
    }
  }
}