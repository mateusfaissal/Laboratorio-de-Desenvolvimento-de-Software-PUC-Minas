package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank(message = "Nome é obrigatório")
  @Column(nullable = false)
  private String nome;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email deve ser válido")
  @Column(nullable = false, unique = true)
  private String email;

  @NotBlank(message = "Senha é obrigatória")
  @Column(nullable = false)
  // Não serializar a senha nas respostas
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String senha;

  @NotBlank(message = "CPF é obrigatório")
  @Column(nullable = false, unique = true)
  private String cpf;

  @NotNull(message = "Tipo de usuário é obrigatório")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoUsuario tipo;

  public enum TipoUsuario {
    ALUNO,
    PROFESSOR,
    EMPRESA
  }

  // Helper method para determinar o tipo de usuário
  @JsonIgnore
  public boolean isAluno() {
    return tipo == TipoUsuario.ALUNO;
  }

  @JsonIgnore
  public boolean isProfessor() {
    return tipo == TipoUsuario.PROFESSOR;
  }

  @JsonIgnore
  public boolean isEmpresa() {
    return tipo == TipoUsuario.EMPRESA;
  }
}