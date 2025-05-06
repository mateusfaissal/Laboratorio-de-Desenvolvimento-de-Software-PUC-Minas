package br.com.moeda_estudantil.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "resgate_cupom")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ResgateCupom extends Transacao {

  @ManyToOne
  @JoinColumn(name = "aluno_id", nullable = false)
  @NotNull(message = "Aluno é obrigatório")
  private Aluno aluno;

  @ManyToOne
  @JoinColumn(name = "vantagem_id", nullable = false)
  @NotNull(message = "Vantagem é obrigatória")
  private Vantagem vantagem;

  @Column(name = "codigo_gerado", nullable = false, unique = true, length = 20)
  private String codigoGerado;

  @Column(name = "data_resgate", nullable = false)
  private LocalDateTime dataResgate;

  @Column(name = "data_utilizacao")
  private LocalDateTime dataUtilizacao;

  @Column(name = "utilizado")
  private Boolean utilizado = false;

  public ResgateCupom() {
    super();
    setTipo(TipoTransacao.RESGATE);
    this.dataResgate = LocalDateTime.now();
    this.codigoGerado = generateCode();
  }

  @PrePersist
  @Override
  public void prePersist() {
    super.prePersist();
    if (this.codigoGerado == null) {
      this.codigoGerado = generateCode();
    }
  }

  private String generateCode() {
    // Gera um código alfanumérico de 8 caracteres
    return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }
}