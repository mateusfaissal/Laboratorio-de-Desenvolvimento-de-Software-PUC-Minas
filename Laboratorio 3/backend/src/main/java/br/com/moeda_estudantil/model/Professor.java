package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "professor")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Professor extends Usuario {

  @ManyToOne
  @JoinColumn(name = "departamento_id", nullable = false)
  @NotNull(message = "Departamento é obrigatório")
  private Departamento departamento;

  @Column(name = "saldo_moedas")
  private Double saldoMoedas = 0.0;

  @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
  private List<TransferenciaAluno> transferencias = new ArrayList<>();

  // Construtor padrão necessário para JPA
  public Professor() {
    super();
    this.setTipo(TipoUsuario.PROFESSOR);
  }
}