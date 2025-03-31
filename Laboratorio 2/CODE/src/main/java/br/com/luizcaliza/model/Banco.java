package br.com.luizcaliza.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bancos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Banco {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private String cnpj;

  @OneToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @OneToMany(mappedBy = "banco")
  private List<ContratoDeCredito> contratosCredito = new ArrayList<>();

  @OneToMany(mappedBy = "banco")
  private List<Automovel> automoveis = new ArrayList<>();

  public void gerarContratoCredito() {
    ContratoDeCredito contrato = new ContratoDeCredito();
    contrato.setBanco(this);
    this.contratosCredito.add(contrato);
  }

  public boolean concederContrato(ContratoDeCredito contrato) {
    contrato.setBanco(this);
    return this.contratosCredito.add(contrato);
  }
}