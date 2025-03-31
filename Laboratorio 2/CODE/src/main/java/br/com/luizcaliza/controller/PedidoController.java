package br.com.luizcaliza.controller;

import br.com.luizcaliza.model.Automovel;
import br.com.luizcaliza.model.Pedido;
import br.com.luizcaliza.services.AutomovelService;
import br.com.luizcaliza.services.PedidoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

  @Autowired
  private PedidoService pedidoService;

  @Autowired
  private AutomovelService automovelService;

  @GetMapping
  public String listarTodos(Model model) {
    List<Pedido> pedidos = pedidoService.listarTodos();
    model.addAttribute("pedidos", pedidos);
    return "pedidos/listar";
  }

  @GetMapping("/{id}")
  public String detalhes(@PathVariable("id") Long id, Model model) {
    Pedido pedido = pedidoService.buscarPorId(id);
    model.addAttribute("pedido", pedido);
    return "pedidos/detalhes";
  }

  @GetMapping("/status/{status}")
  public String listarPorStatus(@PathVariable("status") String status, Model model) {
    List<Pedido> pedidos = pedidoService.buscarPorStatus(status);
    model.addAttribute("pedidos", pedidos);
    model.addAttribute("status", status);
    return "pedidos/por-status";
  }

  @GetMapping("/{id}/automoveis")
  public String automoveis(@PathVariable("id") Long id, Model model) {
    Pedido pedido = pedidoService.buscarPorId(id);
    List<Automovel> automoveis = automovelService.listarTodos();

    model.addAttribute("pedido", pedido);
    model.addAttribute("automoveis", automoveis);

    return "pedidos/selecionar-automovel";
  }

  @GetMapping("/{pedidoId}/associar-automovel/{automovelId}")
  public String associarAutomovel(@PathVariable("pedidoId") Long pedidoId,
      @PathVariable("automovelId") Long automovelId) {
    pedidoService.associarAutomovel(pedidoId, automovelId);
    return "redirect:/pedidos/" + pedidoId;
  }
}