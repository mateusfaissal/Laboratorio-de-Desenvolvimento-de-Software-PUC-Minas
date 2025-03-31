package br.com.luizcaliza.controller;

import br.com.luizcaliza.model.Automovel;
import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.model.Pedido;
import br.com.luizcaliza.model.Rendimento;
import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.services.AutomovelService;
import br.com.luizcaliza.services.ClienteService;
import br.com.luizcaliza.services.PedidoService;
import br.com.luizcaliza.services.RendimentoService;
import br.com.luizcaliza.services.UsuarioService;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private PedidoService pedidoService;

  @Autowired
  private RendimentoService rendimentoService;

  @Autowired
  private AutomovelService automovelService;

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    dateFormat.setLenient(false);
    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
  }

  @GetMapping("/registro")
  public String registroForm(Model model) {
    model.addAttribute("cliente", new Cliente());
    model.addAttribute("usuario", new Usuario());
    return "cliente/registro";
  }

  @PostMapping("/registro")
  public String registrarCliente(@ModelAttribute("cliente") Cliente cliente,
      @ModelAttribute("usuario") Usuario usuario) {
    clienteService.salvar(cliente, usuario);
    return "redirect:/login";
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    List<Pedido> pedidos = clienteService.consultarPedidos(cliente.getId());

    model.addAttribute("cliente", cliente);
    model.addAttribute("pedidos", pedidos);

    return "cliente/dashboard";
  }

  @GetMapping("/pedidos")
  public String pedidos(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    List<Pedido> pedidos = clienteService.consultarPedidos(cliente.getId());

    model.addAttribute("cliente", cliente);
    model.addAttribute("pedidos", pedidos);

    return "cliente/pedidos";
  }

  @GetMapping("/pedidos/novo")
  public String novoPedido(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    // Criar um novo pedido com o cliente atual
    Pedido pedido = new Pedido();
    pedido.setCliente(cliente);
    pedido.setDataInicio(new Date()); // Data atual como padrão para início

    // Adicionar 7 dias para a data de fim padrão
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, 7);
    pedido.setDataFim(cal.getTime());

    // Listar apenas automóveis disponíveis para o período padrão
    List<Automovel> automoveisDisponiveis = automovelService.listarDisponiveisPorPeriodo(
        pedido.getDataInicio(), pedido.getDataFim());

    model.addAttribute("cliente", cliente);
    model.addAttribute("pedido", pedido);
    model.addAttribute("automoveis", automoveisDisponiveis);

    return "cliente/novo-pedido";
  }

  @PostMapping("/pedidos/novo")
  public String criarPedido(@ModelAttribute("pedido") Pedido pedido,
      @RequestParam("dataInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataInicio,
      @RequestParam("dataFim") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataFim,
      Principal principal) {

    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    // Verificar se o automóvel está disponível para o período selecionado
    if (!automovelService.verificarDisponibilidade(pedido.getAutomovel().getId(), dataInicio, dataFim)) {
      return "redirect:/cliente/pedidos/novo?erro=automovel-indisponivel";
    }

    pedido.setCliente(cliente);
    pedido.setDataInicio(dataInicio);
    pedido.setDataFim(dataFim);

    // Calcular valor total com base na diária do automóvel e no número de dias
    long diffInMillies = Math.abs(dataFim.getTime() - dataInicio.getTime());
    long dias = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    dias = Math.max(1, dias); // Garantir pelo menos 1 dia

    Double valorDiaria = pedido.getAutomovel().getPrecoDiaria();
    Double valorTotal = valorDiaria * dias;
    pedido.setValorTotal(valorTotal);

    pedidoService.salvar(pedido);

    return "redirect:/cliente/pedidos?success=pedido-criado";
  }

  @GetMapping("/pedidos/{id}/editar")
  public String editarPedido(@PathVariable("id") Long id, Model model) {
    Pedido pedido = pedidoService.buscarPorId(id);
    model.addAttribute("pedido", pedido);
    return "cliente/editar-pedido";
  }

  @PostMapping("/pedidos/{id}/editar")
  public String atualizarPedido(@PathVariable("id") Long id, @ModelAttribute("pedido") Pedido pedido) {
    try {
      Pedido pedidoExistente = pedidoService.buscarPorId(id);

      if (pedidoExistente == null || !"NOVO".equals(pedidoExistente.getStatus())) {
        return "redirect:/cliente/pedidos?erro=pedido-nao-encontrado";
      }

      // Manter o automóvel original
      pedido.setAutomovel(pedidoExistente.getAutomovel());
      pedido.setId(id);
      pedido.setCliente(pedidoExistente.getCliente());
      pedido.setStatus(pedidoExistente.getStatus());

      // Garantir que o valor total foi calculado corretamente
      if (pedido.getValorTotal() == null || pedido.getValorTotal() <= 0) {
        Automovel automovel = pedido.getAutomovel();
        if (automovel != null && pedido.getDataInicio() != null && pedido.getDataFim() != null) {
          // Calcular a duração em dias
          long diferencaEmMillis = pedido.getDataFim().getTime() - pedido.getDataInicio().getTime();
          int numeroDias = (int) (diferencaEmMillis / (1000 * 60 * 60 * 24)) + 1; // +1 para incluir o dia final

          // Calcular o valor total
          pedido.setValorTotal(automovel.getPrecoDiaria() * numeroDias);
        }
      }

      pedidoService.atualizar(pedido);
      return "redirect:/cliente/pedidos";
    } catch (Exception e) {
      e.printStackTrace();
      return "redirect:/cliente/pedidos?erro=atualizacao-falhou";
    }
  }

  @GetMapping("/pedidos/{id}/cancelar")
  public String cancelarPedido(@PathVariable("id") Long id, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    Pedido pedido = pedidoService.buscarPorId(id);
    clienteService.cancelarPedido(pedido);

    return "redirect:/cliente/pedidos";
  }

  @GetMapping("/pedidos/{id}")
  public String detalhesPedido(@PathVariable("id") Long id, Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    Pedido pedido = pedidoService.buscarPorId(id);

    // Verificar se o pedido pertence ao cliente
    if (pedido != null && pedido.getCliente().getId().equals(cliente.getId())) {
      model.addAttribute("pedido", pedido);
      model.addAttribute("cliente", cliente);
      return "cliente/detalhes-pedido";
    }

    return "redirect:/cliente/pedidos";
  }

  @GetMapping("/rendimentos")
  public String rendimentos(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    List<Rendimento> rendimentos = rendimentoService.buscarPorClienteId(cliente.getId());

    model.addAttribute("cliente", cliente);
    model.addAttribute("rendimentos", rendimentos);
    model.addAttribute("novoRendimento", new Rendimento());

    return "cliente/rendimentos";
  }

  @PostMapping("/rendimentos/salvar")
  public String salvarRendimento(@ModelAttribute("novoRendimento") Rendimento rendimento, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    clienteService.adicionarRendimento(cliente, rendimento);

    return "redirect:/cliente/rendimentos";
  }

  @GetMapping("/perfil")
  public String perfil(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Cliente cliente = usuario.getCliente();

    model.addAttribute("cliente", cliente);

    return "cliente/perfil";
  }

  @PostMapping("/perfil/atualizar")
  public String atualizarPerfil(@ModelAttribute("cliente") Cliente cliente, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    cliente.setId(usuario.getCliente().getId());
    cliente.setUsuario(usuario);

    clienteService.atualizar(cliente);

    return "redirect:/cliente/perfil";
  }
}