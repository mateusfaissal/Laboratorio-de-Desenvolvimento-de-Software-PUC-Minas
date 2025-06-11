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
  private CursoRepository cursoRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private EmpresaParceiraRepository empresaParceiraRepository;

  @Autowired
  private VantagemRepository vantagemRepository;

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

      // Criar curso
      Curso curso = new Curso();
      curso.setNome("Ciência da Computação");
      curso.setInstituicao(instituicao);
      curso = cursoRepository.save(curso);

      // Criar aluno
      Aluno aluno = new Aluno();
      aluno.setNome("João da Silva");
      aluno.setEmail("joao@gmail.com");
      aluno.setSenha(passwordEncoder.encode("123")); // Senha padrão
      aluno.setCpf("123.456.789-00");
      aluno.setRg("MG-12.345.678");
      aluno.setEndereco("Rua do Aluno, 456");
      aluno.setTipo(Usuario.TipoUsuario.ALUNO);
      aluno.setCurso(curso);
      aluno.setSaldoMoedas(1000.0); // Saldo inicial
      aluno = alunoRepository.save(aluno);

      // Criar empresa
      EmpresaParceira empresa = new EmpresaParceira();
      empresa.setNome("Empresa Exemplo");
      empresa.setEmail("empresa@gmail.com");
      empresa.setSenha(passwordEncoder.encode("123")); // Senha padrão
      empresa.setCpf("321.654.987-00"); // CPF do responsável
      empresa.setCnpj("12.345.678/0001-99");
      empresa.setTipo(Usuario.TipoUsuario.EMPRESA);
      empresa.setDescricao("Empresa parceira de exemplo.");
      empresa = empresaParceiraRepository.save(empresa);

      // Criar vantagem
      Vantagem vantagem = new Vantagem();
      vantagem.setNome("Desconto Restaurante");
      vantagem.setDescricao("10% de desconto no restaurante universitário");
      vantagem.setCustoMoedas(50.0);
      vantagem.setEmpresa(empresa);
      vantagemRepository.save(vantagem);

      // Criar professor administrador
      Professor professor = new Professor();
      professor.setNome("Aramuni");
      professor.setEmail("aramuni@gmail.com");
      professor.setSenha(passwordEncoder.encode("123")); // Senha padrão
      professor.setCpf("987.654.321-00");
      professor.setTipo(Usuario.TipoUsuario.PROFESSOR);
      professor.setDepartamento(departamento);
      professor.setSaldoMoedas(1000.0); // Saldo inicial
      professorRepository.save(professor);

      System.out.println("==============================================");
      System.out.println("Dados iniciais criados com sucesso!");
      System.out.println("==============================================");
    };
  }
}