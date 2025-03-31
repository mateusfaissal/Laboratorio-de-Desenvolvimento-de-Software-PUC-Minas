package br.com.luizcaliza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Controller
public class HomeController {

  @GetMapping({ "/", "/home" })
  public String home() {
    return "home";
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/dashboard")
  public String dashboard() {
    // Obter autenticação atual
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // Redirecionar com base no papel do usuário
    if (auth != null && auth.isAuthenticated()) {
      if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        return "redirect:/admin/dashboard";
      } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLIENTE"))) {
        return "redirect:/cliente/dashboard";
      } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BANCO"))) {
        return "redirect:/banco/dashboard";
      }
    }

    // Caso padrão
    return "redirect:/";
  }
}