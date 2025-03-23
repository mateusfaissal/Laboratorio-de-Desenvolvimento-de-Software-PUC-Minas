package br.com.luizcaliza.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Rendimento {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Float valor;
  private String fonte;

  public Float getValor() {
    return valor;
  }

  public void setValor(Float valor) {
    this.valor = valor;
  }

  public String getFonte() {
    return fonte;
  }

  public void setFonte(String fonte) {
    this.fonte = fonte;
  }

}
