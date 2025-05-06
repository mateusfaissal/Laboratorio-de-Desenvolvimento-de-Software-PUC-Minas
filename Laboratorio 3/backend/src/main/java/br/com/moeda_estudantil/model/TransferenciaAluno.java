package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "transferencia_aluno")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TransferenciaAluno extends Transacao {

  @ManyToOne
  @JoinColumn(name = "professor_id", nullable = false)
  private Professor professor;

  @ManyToOne
  @JoinColumn(name = "aluno_id", nullable = false)
  @NotNull(message = "Aluno é obrigatório")
  private Aluno aluno;

  @NotBlank(message = "Motivo do reconhecimento é obrigatório")
  @Column(name = "motivo_reconhecimento", nullable = false, length = 500)
  private String motivoReconhecimento;

  public TransferenciaAluno() {
    super();
    setTipo(TipoTransacao.TRANSFERENCIA);
  }
}