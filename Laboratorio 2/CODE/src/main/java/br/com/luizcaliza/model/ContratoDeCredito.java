package br.com.luizcaliza.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "contratos_credito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContratoDeCredito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Temporal(TemporalType.DATE)
  @Column(nullable = false)
  private Date data;

  @Column(nullable = false)
  private Float valor;

  @Column(nullable = false)
  private String condicoes;

  @ManyToOne
  @JoinColumn(name = "banco_id")
  private Banco banco;

  @OneToMany(mappedBy = "contrato")
  private List<Pedido> pedidos = new ArrayList<>();

  private boolean ativo = false;

  public boolean gerarContrato() {
    // Lógica para gerar o contrato
    return true;
  }

  public boolean validarContrato() {
    // Validação do contrato
    return true;
  }

  @PrePersist
  protected void onCreate() {
    data = new Date();
  }
}