package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TransferenciaAluno.class, name = "TRANSFERENCIA"),
    @JsonSubTypes.Type(value = ResgateCupom.class, name = "RESGATE")
})
public abstract class Transacao {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "data_hora", nullable = false)
  private LocalDateTime dataHora;

  @NotNull(message = "Valor é obrigatório")
  @Positive(message = "Valor deve ser positivo")
  @Column(nullable = false)
  private Double valor;

  @Column(length = 1000)
  private String descricao;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoTransacao tipo;

  public enum TipoTransacao {
    TRANSFERENCIA,
    RESGATE
  }

  @PrePersist
  public void prePersist() {
    dataHora = LocalDateTime.now();
  }
}
