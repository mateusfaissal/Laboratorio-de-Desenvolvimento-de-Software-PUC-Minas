package br.com.moeda_estudantil.model;

import lombok.Data;

@Data
public class LoginResponse {
  private String token;
  private String id;
  private String nome;
  private String email;
  private Usuario.TipoUsuario tipo;
}
