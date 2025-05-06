package br.com.moeda_estudantil.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "empresa_parceira")
@Data
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EmpresaParceira extends Usuario {

  @NotBlank(message = "CNPJ é obrigatório")
  @Column(nullable = false, unique = true)
  private String cnpj;

  @Column(columnDefinition = "TEXT")
  private String descricao;

  @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Vantagem> vantagens = new ArrayList<>();

  // Construtor padrão necessário para JPA
  public EmpresaParceira() {
    super();
    this.setTipo(TipoUsuario.EMPRESA);
  }
}