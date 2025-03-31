package br.com.luizcaliza.controller;

import br.com.luizcaliza.model.Automovel;
import br.com.luizcaliza.model.Banco;
import br.com.luizcaliza.model.ContratoDeCredito;
import br.com.luizcaliza.model.Pedido;
import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.services.AutomovelService;
import br.com.luizcaliza.services.BancoService;
import br.com.luizcaliza.services.ContratoDeCreditoService;
import br.com.luizcaliza.services.PedidoService;
import br.com.luizcaliza.services.UsuarioService;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.model.Rendimento;
import br.com.luizcaliza.services.RendimentoService;

@Controller
@RequestMapping("/banco")
public class BancoController {

  private static final Logger log = Logger.getLogger(BancoController.class.getName());

  @Autowired
  private BancoService bancoService;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private ContratoDeCreditoService contratoDeCreditoService;

  @Autowired
  private AutomovelService automovelService;

  @Autowired
  private PedidoService pedidoService;

  @Autowired
  private RendimentoService rendimentoService;

  @GetMapping("/registro")
  public String registroForm(Model model) {
    model.addAttribute("banco", new Banco());
    model.addAttribute("usuario", new Usuario());
    return "banco/registro";
  }

  @PostMapping("/registro")
  public String registrarBanco(@ModelAttribute("banco") Banco banco,
      @ModelAttribute("usuario") Usuario usuario) {
    bancoService.salvar(banco, usuario);
    return "redirect:/login";
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model, Principal principal) {
    try {
      log.info("Iniciando carregamento do dashboard do banco");

      if (principal == null) {
        log.severe("Principal é nulo no dashboard do banco");
        return "redirect:/login";
      }

      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null) {
        log.severe("Usuário não encontrado: " + principal.getName());
        return "redirect:/login";
      }

      Banco banco = usuario.getBanco();
      if (banco == null) {
        log.severe("Banco não encontrado para o usuário: " + principal.getName());
        return "redirect:/login";
      }

      log.info("Carregando dados do banco: " + banco.getNome());

      List<ContratoDeCredito> contratos = contratoDeCreditoService.buscarPorBancoId(banco.getId());
      List<Automovel> automoveis = automovelService.listarPorBanco(banco.getId());

      // Buscar pedidos pendentes para análise
      List<Pedido> pedidosPendentes = pedidoService.buscarPorAutomovelBancoId(banco.getId())
          .stream()
          .filter(p -> p.getStatus().equals("NOVO"))
          .toList();

      // Buscar pedidos vigentes (aprovados e com data de fim não ultrapassada)
      List<Pedido> pedidosVigentes = pedidoService.buscarPorAutomovelBancoId(banco.getId())
          .stream()
          .filter(p -> p.getStatus().equals("APROVADO") &&
              p.getDataFim() != null &&
              p.getDataFim().after(new java.util.Date()))
          .toList();

      log.info("Contratos: " + contratos.size() + ", Automóveis: " + automoveis.size() +
          ", Pedidos pendentes: " + pedidosPendentes.size() + ", Pedidos vigentes: " + pedidosVigentes.size());

      model.addAttribute("banco", banco);
      model.addAttribute("contratos", contratos);
      model.addAttribute("automoveis", automoveis);
      model.addAttribute("pedidosPendentes", pedidosPendentes);
      model.addAttribute("pedidosVigentes", pedidosVigentes);

      log.info("Dashboard do banco carregado com sucesso");

      return "banco/dashboard";
    } catch (Exception e) {
      log.severe("Erro ao carregar dashboard do banco: " + e.getMessage());
      e.printStackTrace();
      return "error";
    }
  }

  @GetMapping("/contratos")
  public String contratos(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Banco banco = usuario.getBanco();

    List<ContratoDeCredito> contratos = bancoService.listarContratos(banco.getId());

    model.addAttribute("banco", banco);
    model.addAttribute("contratos", contratos);
    model.addAttribute("novoContrato", new ContratoDeCredito());

    return "banco/contratos";
  }

  @GetMapping("/contratos/novo")
  public String novoContrato(Model model) {
    model.addAttribute("contrato", new ContratoDeCredito());
    return "banco/novo-contrato";
  }

  @PostMapping("/contratos/salvar")
  public String salvarContrato(@ModelAttribute("contrato") ContratoDeCredito contrato, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Banco banco = usuario.getBanco();

    contrato.setBanco(banco);
    contratoDeCreditoService.salvar(contrato);

    return "redirect:/banco/contratos";
  }

  @GetMapping("/contratos/{id}/validar")
  public String validarContrato(@PathVariable("id") Long id) {
    contratoDeCreditoService.validarContrato(id);
    return "redirect:/banco/contratos";
  }

  @GetMapping("/automoveis")
  public String automoveis(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Banco banco = usuario.getBanco();

    List<Automovel> automoveis = automovelService.listarPorBanco(banco.getId());

    model.addAttribute("banco", banco);
    model.addAttribute("automoveis", automoveis);

    return "banco/automoveis";
  }

  @GetMapping("/automoveis/{id}/detalhes")
  public String detalhesAutomovel(@PathVariable("id") Long id, Model model) {
    Automovel automovel = automovelService.buscarPorId(id);
    model.addAttribute("automovel", automovel);
    return "banco/detalhes-automovel";
  }

  @GetMapping("/automoveis/novo")
  public String novoAutomovel(Model model) {
    model.addAttribute("automovel", new Automovel());
    return "banco/novo-automovel";
  }

  @PostMapping("/automoveis/salvar")
  public String salvarAutomovel(@ModelAttribute("automovel") Automovel automovel, Principal principal) {
    try {
      log.info("Salvando novo automóvel");

      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null || usuario.getBanco() == null) {
        return "redirect:/banco/dashboard?erro=banco-nao-encontrado";
      }

      Banco banco = usuario.getBanco();
      automovel.setBanco(banco);

      automovelService.salvar(automovel);

      return "redirect:/banco/automoveis?success=automovel-cadastrado";
    } catch (Exception e) {
      log.severe("Erro ao salvar automóvel: " + e.getMessage());
      e.printStackTrace();
      return "redirect:/banco/automoveis/novo?error=true";
    }
  }

  @GetMapping("/pedidos")
  public String listarPedidos(Model model, Principal principal) {
    Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
    Banco banco = usuario.getBanco();

    List<Pedido> pedidosPendentes = pedidoService.buscarPorAutomovelBancoId(banco.getId())
        .stream()
        .filter(p -> p.getStatus().equals("NOVO"))
        .toList();

    // Buscar pedidos vigentes (aprovados e com data de fim não ultrapassada)
    List<Pedido> pedidosVigentes = pedidoService.buscarPorAutomovelBancoId(banco.getId())
        .stream()
        .filter(p -> p.getStatus().equals("APROVADO") &&
            p.getDataFim() != null &&
            p.getDataFim().after(new java.util.Date()))
        .toList();

    // Buscar pedidos recusados
    List<Pedido> pedidosRecusados = pedidoService.buscarPorAutomovelBancoId(banco.getId())
        .stream()
        .filter(p -> p.getStatus().equals("RECUSADO"))
        .toList();

    model.addAttribute("banco", banco);
    model.addAttribute("pedidos", pedidosPendentes);
    model.addAttribute("pedidosVigentes", pedidosVigentes);
    model.addAttribute("pedidosRecusados", pedidosRecusados);

    return "banco/pedidos";
  }

  @GetMapping("/pedidos/{id}")
  public String verPedido(@PathVariable("id") Long id, Model model, Principal principal) {
    try {
      // Buscar o banco logado
      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null || usuario.getBanco() == null) {
        return "redirect:/banco/dashboard?erro=banco-nao-encontrado";
      }
      Banco banco = usuario.getBanco();

      // Buscar o pedido pelo ID
      Pedido pedido = pedidoService.buscarPorId(id);
      if (pedido == null) {
        return "redirect:/banco/pedidos?erro=pedido-nao-encontrado";
      }

      // Verificar se o pedido pertence ao banco
      if (pedido.getAutomovel() == null || !pedido.getAutomovel().getBanco().getId().equals(banco.getId())) {
        return "redirect:/banco/pedidos?erro=pedido-nao-autorizado";
      }

      // Adicionar detalhes do cliente e rendimentos
      Cliente cliente = pedido.getCliente();
      List<Rendimento> rendimentos = rendimentoService.buscarPorClienteId(cliente.getId());
      double rendaTotal = rendimentos.stream().mapToDouble(Rendimento::getValor).sum();

      model.addAttribute("pedido", pedido);
      model.addAttribute("cliente", cliente);
      model.addAttribute("rendimentos", rendimentos);
      model.addAttribute("rendaTotal", rendaTotal);

      return "banco/detalhes-pedido";
    } catch (Exception e) {
      log.severe("Erro ao visualizar pedido: " + e.getMessage());
      return "redirect:/banco/pedidos?erro=erro-processar-pedido";
    }
  }

  @GetMapping("/pedidos/{id}/aprovar")
  public String aprovarPedidoForm(@PathVariable("id") Long id, Model model, Principal principal) {
    try {
      // Buscar o banco logado
      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null || usuario.getBanco() == null) {
        return "redirect:/banco/dashboard?erro=banco-nao-encontrado";
      }
      Banco banco = usuario.getBanco();

      // Buscar o pedido pelo ID
      Pedido pedido = pedidoService.buscarPorId(id);
      if (pedido == null) {
        return "redirect:/banco/pedidos?erro=pedido-nao-encontrado";
      }

      // Verificar se o pedido pertence ao banco e está no status NOVO
      if (pedido.getAutomovel() == null ||
          !pedido.getAutomovel().getBanco().getId().equals(banco.getId()) ||
          !"NOVO".equals(pedido.getStatus())) {
        return "redirect:/banco/pedidos?erro=pedido-nao-autorizado";
      }

      return "redirect:/banco/pedidos/" + id;
    } catch (Exception e) {
      log.severe("Erro ao acessar formulário de aprovação: " + e.getMessage());
      return "redirect:/banco/pedidos?erro=erro-processar-pedido";
    }
  }

  @PostMapping("/pedidos/{id}/aprovar")
  public String aprovarPedido(
      @PathVariable("id") Long id,
      @RequestParam(value = "gerarContrato", required = false) Boolean gerarContrato,
      @RequestParam(value = "proprietarioCarro", required = false, defaultValue = "BANCO") String proprietarioCarro,
      Principal principal) {
    try {
      // Buscar o banco logado
      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null || usuario.getBanco() == null) {
        return "redirect:/banco/dashboard?erro=banco-nao-encontrado";
      }
      Banco banco = usuario.getBanco();

      // Buscar o pedido pelo ID
      Pedido pedido = pedidoService.buscarPorId(id);
      if (pedido == null) {
        return "redirect:/banco/pedidos?erro=pedido-nao-encontrado";
      }

      // Verificar se o pedido pertence ao banco e está no status NOVO
      if (pedido.getAutomovel() == null ||
          !pedido.getAutomovel().getBanco().getId().equals(banco.getId()) ||
          !"NOVO".equals(pedido.getStatus())) {
        return "redirect:/banco/pedidos?erro=pedido-nao-autorizado";
      }

      // Definir o proprietário do carro
      pedidoService.definirProprietarioCarro(id, proprietarioCarro);

      // Atualizar o status do pedido para APROVADO
      pedidoService.atualizarStatus(id, "APROVADO");

      // Gerar o contrato de crédito apenas se a opção estiver marcada
      if (gerarContrato != null && gerarContrato) {
        // Gerar o contrato de crédito
        ContratoDeCredito contrato = new ContratoDeCredito();
        contrato.setBanco(banco);
        contrato.setValor(pedido.getValorTotal().floatValue());
        contrato.setCondicoes("Automóvel: " + pedido.getAutomovel().getDetalhes() +
            " | Período: " +
            (pedido.getDataInicio() != null ? pedido.getDataInicio() : "Não definido") +
            " até " +
            (pedido.getDataFim() != null ? pedido.getDataFim() : "Não definido") +
            " | Proprietário do carro: " + pedido.getProprietarioCarro());
        contrato = contratoDeCreditoService.salvar(contrato);

        // Associar o contrato ao pedido
        pedidoService.associarContrato(id, contrato.getId());

        // Validar o contrato (torná-lo ativo)
        contratoDeCreditoService.validarContrato(contrato.getId());
      }

      return "redirect:/banco/pedidos?success=pedido-aprovado";
    } catch (Exception e) {
      log.severe("Erro ao aprovar pedido: " + e.getMessage());
      return "redirect:/banco/pedidos?erro=erro-processar-pedido";
    }
  }

  @GetMapping("/pedidos/{id}/recusar")
  public String recusarPedido(@PathVariable("id") Long id, Model model, Principal principal) {
    try {
      // Buscar o banco logado
      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null || usuario.getBanco() == null) {
        return "redirect:/banco/dashboard?erro=banco-nao-encontrado";
      }
      Banco banco = usuario.getBanco();

      // Buscar o pedido pelo ID
      Pedido pedido = pedidoService.buscarPorId(id);
      if (pedido == null) {
        return "redirect:/banco/pedidos?erro=pedido-nao-encontrado";
      }

      // Verificar se o pedido pertence ao banco e está no status NOVO
      if (pedido.getAutomovel() == null ||
          !pedido.getAutomovel().getBanco().getId().equals(banco.getId()) ||
          !"NOVO".equals(pedido.getStatus())) {
        return "redirect:/banco/pedidos?erro=pedido-nao-autorizado";
      }

      model.addAttribute("pedido", pedido);
      return "banco/recusar-pedido";
    } catch (Exception e) {
      log.severe("Erro ao acessar formulário de recusa: " + e.getMessage());
      return "redirect:/banco/pedidos?erro=erro-processar-pedido";
    }
  }

  @PostMapping("/pedidos/{id}/recusar")
  public String processarRecusaPedido(
      @PathVariable("id") Long id,
      @RequestParam("motivoRecusa") String motivoRecusa,
      Principal principal) {
    try {
      // Buscar o banco logado
      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null || usuario.getBanco() == null) {
        return "redirect:/banco/dashboard?erro=banco-nao-encontrado";
      }
      Banco banco = usuario.getBanco();

      // Buscar o pedido pelo ID
      Pedido pedido = pedidoService.buscarPorId(id);
      if (pedido == null) {
        return "redirect:/banco/pedidos?erro=pedido-nao-encontrado";
      }

      // Verificar se o pedido pertence ao banco e está no status NOVO
      if (pedido.getAutomovel() == null ||
          !pedido.getAutomovel().getBanco().getId().equals(banco.getId()) ||
          !"NOVO".equals(pedido.getStatus())) {
        return "redirect:/banco/pedidos?erro=pedido-nao-autorizado";
      }

      // Atualizar o motivo da recusa
      pedido.setMotivoRecusa(motivoRecusa);

      // Atualizar o status do pedido para RECUSADO
      pedidoService.atualizarStatus(id, "RECUSADO");
      pedidoService.salvar(pedido);

      return "redirect:/banco/pedidos?success=pedido-recusado";
    } catch (Exception e) {
      log.severe("Erro ao recusar pedido: " + e.getMessage());
      return "redirect:/banco/pedidos?erro=erro-processar-pedido";
    }
  }

  @GetMapping("/perfil")
  public String perfil(Model model, Principal principal) {
    try {
      log.info("Acessando perfil do banco");

      if (principal == null) {
        log.severe("Principal é nulo no perfil do banco");
        return "redirect:/login";
      }

      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null) {
        log.severe("Usuário não encontrado: " + principal.getName());
        return "redirect:/login";
      }

      Banco banco = usuario.getBanco();
      if (banco == null) {
        log.severe("Banco não encontrado para o usuário: " + principal.getName());
        return "redirect:/login";
      }

      log.info("Carregando perfil do banco: " + banco.getNome());
      model.addAttribute("banco", banco);

      return "banco/perfil";
    } catch (Exception e) {
      log.severe("Erro ao carregar perfil do banco: " + e.getMessage());
      e.printStackTrace();
      return "redirect:/banco/dashboard?erro=erro-carregar-perfil";
    }
  }

  @PostMapping("/perfil/atualizar")
  public String atualizarPerfil(@ModelAttribute("banco") Banco banco, Principal principal) {
    try {
      log.info("Atualizando perfil do banco");

      if (principal == null) {
        log.severe("Principal é nulo ao atualizar perfil do banco");
        return "redirect:/login";
      }

      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null) {
        log.severe("Usuário não encontrado ao atualizar perfil: " + principal.getName());
        return "redirect:/login";
      }

      if (usuario.getBanco() == null) {
        log.severe("Banco não encontrado para o usuário: " + principal.getName());
        return "redirect:/login";
      }

      banco.setId(usuario.getBanco().getId());
      banco.setUsuario(usuario);

      log.info("Salvando alterações do banco: " + banco.getNome());
      bancoService.atualizar(banco);

      return "redirect:/banco/perfil?success=true";
    } catch (Exception e) {
      log.severe("Erro ao atualizar perfil do banco: " + e.getMessage());
      e.printStackTrace();
      return "redirect:/banco/perfil?error=true";
    }
  }

  @GetMapping("/contratos/{id}")
  public String detalheContrato(@PathVariable("id") Long id, Model model, Principal principal) {
    try {
      // Buscar o banco logado
      Usuario usuario = usuarioService.buscarPorUsername(principal.getName());
      if (usuario == null || usuario.getBanco() == null) {
        return "redirect:/banco/dashboard?erro=banco-nao-encontrado";
      }
      Banco banco = usuario.getBanco();

      // Buscar o contrato pelo ID
      ContratoDeCredito contrato = contratoDeCreditoService.buscarPorId(id);
      if (contrato == null) {
        return "redirect:/banco/contratos?erro=contrato-nao-encontrado";
      }

      // Verificar se o contrato pertence ao banco
      if (!contrato.getBanco().getId().equals(banco.getId())) {
        return "redirect:/banco/contratos?erro=operacao-nao-permitida";
      }

      model.addAttribute("contrato", contrato);

      return "banco/detalhes-contrato";
    } catch (Exception e) {
      log.severe("Erro ao visualizar contrato: " + e.getMessage());
      return "redirect:/banco/contratos?erro=erro-processar";
    }
  }
}