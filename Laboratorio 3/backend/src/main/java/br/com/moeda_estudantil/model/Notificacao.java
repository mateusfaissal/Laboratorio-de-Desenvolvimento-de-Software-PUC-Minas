package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Notificacao {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank(message = "Destinatário é obrigatório")
  @Column(nullable = false)
  private String destinatario;

  @NotBlank(message = "Assunto é obrigatório")
  @Column(nullable = false)
  private String assunto;

  @Column(columnDefinition = "TEXT")
  private String conteudo;

  @Column(name = "data_envio", nullable = false)
  private LocalDateTime dataEnvio;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusNotificacao status;

  @ManyToOne
  @JoinColumn(name = "resgate_id")
  private ResgateCupom resgate;

  public enum StatusNotificacao {
    PENDENTE,
    ENVIADO,
    FALHA
  }

  @PrePersist
  public void prePersist() {
    dataEnvio = LocalDateTime.now();
    if (status == null) {
      status = StatusNotificacao.PENDENTE;
    }
  }
}