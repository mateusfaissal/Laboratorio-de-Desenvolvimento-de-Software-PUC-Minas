package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "aluno")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Aluno extends Usuario {

  @NotBlank(message = "RG é obrigatório")
  @Column(nullable = false)
  private String rg;

  @NotBlank(message = "Endereço é obrigatório")
  @Column(nullable = false)
  private String endereco;

  @NotNull(message = "Curso é obrigatório")
  @ManyToOne
  @JoinColumn(name = "curso_id", nullable = false)
  private Curso curso;

  @Column(name = "saldo_moedas")
  private Double saldoMoedas = 0.0;

  // Construtor padrão necessário para JPA
  public Aluno() {
    super();
    this.setTipo(TipoUsuario.ALUNO);
  }
}