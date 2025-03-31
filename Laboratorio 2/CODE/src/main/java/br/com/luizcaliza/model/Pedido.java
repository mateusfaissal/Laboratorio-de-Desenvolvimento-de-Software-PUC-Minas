package br.com.luizcaliza.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date dataCriacao;

  @Column(nullable = false)
  private String status;

  @ManyToOne
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @ManyToOne
  @JoinColumn(name = "automovel_id", nullable = false)
  private Automovel automovel;

  @ManyToOne
  @JoinColumn(name = "contrato_id")
  private ContratoDeCredito contrato;

  @Temporal(TemporalType.DATE)
  private Date dataInicio;

  @Temporal(TemporalType.DATE)
  private Date dataFim;

  @Column(nullable = false)
  private Double valorTotal;

  @Column
  private String proprietarioCarro;

  @Column
  private String motivoRecusa;

  public void atualizarStatus(String status) {
    this.status = status;
  }

  public String getDetalhes() {
    return "Pedido #" + id + " - Cliente: " + cliente.getNome() +
        " - Automóvel: " + automovel.getDetalhes() +
        " - Status: " + status;
  }

  public void associarAutomovel(Automovel automovel) {
    this.automovel = automovel;
    automovel.getPedidos().add(this);
  }

  public void associarContrato(ContratoDeCredito contrato) {
    this.contrato = contrato;
    contrato.getPedidos().add(this);
  }

  @PrePersist
  protected void onCreate() {
    dataCriacao = new Date();
    if (status == null) {
      status = "NOVO";
    }
  }
}