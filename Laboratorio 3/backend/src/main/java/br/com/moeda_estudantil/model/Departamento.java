package br.com.moeda_estudantil.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departamento")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Departamento {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @NotBlank(message = "Nome do departamento é obrigatório")
  @Column(nullable = false)
  private String nome;

  @ManyToOne
  @JoinColumn(name = "instituicao_id", nullable = false)
  private Instituicao instituicao;

  @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
  private List<Professor> professores = new ArrayList<>();
}