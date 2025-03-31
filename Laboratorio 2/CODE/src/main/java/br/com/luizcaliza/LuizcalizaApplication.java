package br.com.luizcaliza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação principal do sistema.
 * 
 * Nota sobre dependências circulares:
 * Esta aplicação contém dependências circulares entre serviços que foram
 * resolvidas de duas formas:
 * 1. Adição da propriedade spring.main.allow-circular-references=true no
 * application.properties
 * 2. Uso da anotação @Lazy nas injeções de dependência para quebrar os ciclos
 * 
 * Uma melhor solução a longo prazo seria reorganizar a arquitetura para evitar
 * estas dependências circulares,
 * possivelmente extraindo lógicas comuns para serviços compartilhados ou usando
 * padrões como eventos.
 */
@SpringBootApplication
public class LuizcalizaApplication {

  public static void main(String[] args) {
    SpringApplication.run(LuizcalizaApplication.class, args);
  }

}
