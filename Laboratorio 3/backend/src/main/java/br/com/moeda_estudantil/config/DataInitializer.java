package br.com.moeda_estudantil.config;

import br.com.moeda_estudantil.model.*;
import br.com.moeda_estudantil.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private ProfessorRepository professorRepository;

  @Autowired
  private InstituicaoRepository instituicaoRepository;

  @Autowired
  private DepartamentoRepository departamentoRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  /**
   * Inicializa dados para ambiente de desenvolvimento e teste
   * O perfil "prod" não carrega estes dados
   */
  @Bean
  @Profile({ "dev", "test", "default" })
  public CommandLineRunner initData() {
    return args -> {
      // Verificar se já existem dados (não recriar se já existirem)
      if (usuarioRepository.count() > 0) {
        return;
      }

      // Criar instituição
      Instituicao instituicao = new Instituicao();
      instituicao.setNome("Universidade Exemplo");
      instituicao.setEndereco("Rua da Universidade, 123");
      instituicao = instituicaoRepository.save(instituicao);

      // Criar departamento
      Departamento departamento = new Departamento();
      departamento.setNome("Departamento de Ciência da Computação");
      departamento.setInstituicao(instituicao);
      departamento = departamentoRepository.save(departamento);

      // Criar professor administrador
      Professor admin = new Professor();
      admin.setNome("Admin do Sistema");
      admin.setEmail("admin@example.com");
      admin.setSenha(passwordEncoder.encode("admin123")); // Senha padrão
      admin.setCpf("123.456.789-00");
      admin.setTipo(Usuario.TipoUsuario.PROFESSOR);
      admin.setDepartamento(departamento);
      admin.setSaldoMoedas(1000.0); // Saldo inicial
      professorRepository.save(admin);

      System.out.println("==============================================");
      System.out.println("Dados iniciais criados com sucesso!");
      System.out.println("Usuário de acesso:");
      System.out.println("Email: admin@example.com");
      System.out.println("Senha: admin123");
      System.out.println("==============================================");
    };
  }
}