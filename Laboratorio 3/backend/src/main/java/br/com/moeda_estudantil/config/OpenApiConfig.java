package br.com.moeda_estudantil.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String SCHEME_NAME = "bearerAuth";
  private static final String SCHEME = "bearer";

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("API do Sistema de Moeda Estudantil")
            .description(
                "API RESTful para o Sistema de Moeda Estudantil, permitindo gerenciamento de alunos, professores, empresas parceiras e transações de moedas virtuais.")
            .version("1.0.0")
            .contact(new Contact()
                .name("Equipe de Desenvolvimento")
                .email("contato@moedaestudantil.com.br")
                .url("https://www.moedaestudantil.com.br"))
            .license(new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT")))
        .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
        .components(new Components()
            .addSecuritySchemes(SCHEME_NAME, createSecurityScheme()));
  }

  private SecurityScheme createSecurityScheme() {
    return new SecurityScheme()
        .name(SCHEME_NAME)
        .type(SecurityScheme.Type.HTTP)
        .scheme(SCHEME)
        .bearerFormat("JWT");
  }
}