package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "vantagem")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Vantagem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank(message = "Nome da vantagem é obrigatório")
  @Column(nullable = false)
  private String nome;

  @Column(columnDefinition = "TEXT")
  private String descricao;

  @Column(name = "foto_url")
  private String fotoUrl;

  @NotNull(message = "Custo em moedas é obrigatório")
  @Min(value = 1, message = "O custo deve ser maior que zero")
  @Column(name = "custo_moedas", nullable = false)
  private Double custoMoedas;

  @ManyToOne
  @JoinColumn(name = "empresa_id", nullable = false)
  private EmpresaParceira empresa;
}