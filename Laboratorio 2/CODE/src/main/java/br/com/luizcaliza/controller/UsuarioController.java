package br.com.luizcaliza.controller;

import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registro")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @GetMapping
  public String registroForm(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "registro";
  }

  @GetMapping("/cliente")
  public String registroCliente(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "registro-cliente";
  }

  @GetMapping("/banco")
  public String registroBanco(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "registro-banco";
  }

  @PostMapping("/salvar")
  public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario) {
    usuarioService.salvar(usuario);
    return "redirect:/login";
  }
}