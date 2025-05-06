package br.com.moeda_estudantil.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlterarSenhaRequest {

  @NotBlank(message = "Senha atual é obrigatória")
  private String senhaAtual;

  @NotBlank(message = "Nova senha é obrigatória")
  @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
  private String novaSenha;
}