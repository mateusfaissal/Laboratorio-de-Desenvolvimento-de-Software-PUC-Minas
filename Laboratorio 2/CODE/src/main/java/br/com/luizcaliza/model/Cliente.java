package br.com.luizcaliza.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cliente extends Usuario {
  private String nome;
  private String cpf;
  private String rg;
  private String profissao;

  @OneToOne
  private Endereco endereco;

  @OneToMany
  @JoinColumn(name = "cliente_id")
  private List<Rendimento> rendimentos;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getProfissao() {
    return profissao;
  }

  public void setProfissao(String profissao) {
    this.profissao = profissao;
  }

  public Endereco getEndereco() {
    return endereco;
  }

  public void setEndereco(Endereco endereco) {
    this.endereco = endereco;
  }

  public List<Rendimento> getRendimentos() {
    return rendimentos;
  }

  public void setRendimentos(List<Rendimento> rendimentos) {
    this.rendimentos = rendimentos;
  }
}
