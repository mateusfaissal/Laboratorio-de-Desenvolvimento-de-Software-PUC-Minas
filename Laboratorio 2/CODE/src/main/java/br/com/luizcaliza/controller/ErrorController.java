package br.com.luizcaliza.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

  private static final Logger log = Logger.getLogger(ErrorController.class.getName());

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    String errorMsg = "Ocorreu um erro desconhecido";
    int statusCode = 500;

    if (status != null) {
      statusCode = Integer.parseInt(status.toString());
      log.severe("Erro " + statusCode + " ocorreu em: " + request.getRequestURL());

      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        errorMsg = "Página não encontrada";
      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
        errorMsg = "Acesso negado";
      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
        errorMsg = "Erro interno do servidor";

        // Log the exception
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        if (throwable != null) {
          log.severe("Exceção: " + throwable.getMessage());
          throwable.printStackTrace();
          model.addAttribute("exception", throwable.getMessage());
        }
      }
    }

    model.addAttribute("status", statusCode);
    model.addAttribute("message", errorMsg);
    model.addAttribute("path", request.getRequestURI());

    return "error";
  }
}