package br.com.luizcaliza.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "automoveis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Automovel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String matricula;

  @Column(nullable = false)
  private Integer ano;

  @Column(nullable = false)
  private String marca;

  @Column(nullable = false)
  private String modelo;

  @Column(nullable = false)
  private String placa;

  @Column(nullable = false)
  private Double precoDiaria;

  @ManyToOne
  @JoinColumn(name = "proprietario_id")
  private Usuario proprietario;

  @ManyToOne
  @JoinColumn(name = "cliente_id")
  private Cliente cliente;

  @ManyToOne
  @JoinColumn(name = "banco_id")
  private Banco banco;

  @OneToMany(mappedBy = "automovel")
  private List<Pedido> pedidos = new ArrayList<>();

  public String getDetalhes() {
    return marca + " " + modelo + " (" + ano + ") - Placa: " + placa;
  }

  public void atualizarDados(String matricula, Integer ano, String marca, String modelo, String placa,
      Double precoDiaria) {
    this.matricula = matricula;
    this.ano = ano;
    this.marca = marca;
    this.modelo = modelo;
    this.placa = placa;
    this.precoDiaria = precoDiaria;
  }

  public void setProprietario(Usuario usuario) {
    this.proprietario = usuario;
  }
}
