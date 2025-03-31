package br.com.luizcaliza.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String rg;

  @Column(nullable = false)
  private String cpf;

  @Column(nullable = false)
  private String nome;

  @Column(nullable = false)
  private String endereco;

  private String profissao;

  @OneToOne
  @JoinColumn(name = "usuario_id")
  private Usuario usuario;

  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
  private List<Rendimento> rendimentos = new ArrayList<>();

  @OneToMany(mappedBy = "cliente")
  private List<Pedido> pedidos = new ArrayList<>();

  @OneToMany(mappedBy = "cliente")
  private List<Automovel> automoveis = new ArrayList<>();

  public boolean criarPedido(Pedido pedido) {
    pedido.setCliente(this);
    return this.pedidos.add(pedido);
  }

  public boolean consultarPedidos() {
    return !this.pedidos.isEmpty();
  }

  public boolean modificarPedido(Pedido pedido) {
    if (this.pedidos.contains(pedido)) {
      // Lógica para modificar o pedido
      return true;
    }
    return false;
  }

  public boolean cancelarPedido(Pedido pedido) {
    if (this.pedidos.contains(pedido)) {
      pedido.setStatus("CANCELADO");
      return true;
    }
    return false;
  }

  public boolean associarContrato(Pedido pedido, ContratoDeCredito contrato) {
    if (this.pedidos.contains(pedido)) {
      pedido.setContrato(contrato);
      return true;
    }
    return false;
  }
}