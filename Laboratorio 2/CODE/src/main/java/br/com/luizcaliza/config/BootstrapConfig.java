package br.com.luizcaliza.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.luizcaliza.model.Automovel;
import br.com.luizcaliza.model.Banco;
import br.com.luizcaliza.model.Cliente;
import br.com.luizcaliza.model.Rendimento;
import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.repositories.AutomovelRepository;
import br.com.luizcaliza.repositories.BancoRepository;
import br.com.luizcaliza.repositories.ClienteRepository;
import br.com.luizcaliza.repositories.RendimentoRepository;
import br.com.luizcaliza.repositories.UsuarioRepository;

@Configuration
public class BootstrapConfig {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Bean
  public CommandLineRunner iniciarDados(
      UsuarioRepository usuarioRepository,
      ClienteRepository clienteRepository,
      BancoRepository bancoRepository,
      AutomovelRepository automovelRepository,
      RendimentoRepository rendimentoRepository) {
    return args -> {
      // Criar usuário admin
      Usuario admin = new Usuario();
      admin.setUsername("admin");
      admin.setSenha(passwordEncoder.encode("admin"));
      admin.setRole("ADMIN");
      usuarioRepository.save(admin);

      // Criar usuário cliente
      Usuario usuarioCliente = new Usuario();
      usuarioCliente.setUsername("cliente");
      usuarioCliente.setSenha(passwordEncoder.encode("cliente"));
      usuarioCliente.setRole("CLIENTE");
      usuarioRepository.save(usuarioCliente);

      // Criar cliente
      Cliente cliente = new Cliente();
      cliente.setNome("João Silva");
      cliente.setCpf("123.456.789-00");
      cliente.setRg("12.345.678-9");
      cliente.setEndereco("Rua Exemplo, 123");
      cliente.setProfissao("Engenheiro");
      cliente.setUsuario(usuarioCliente);
      clienteRepository.save(cliente);

      // Criar rendimentos para o cliente
      Rendimento rendimento1 = new Rendimento();
      rendimento1.setCliente(cliente);
      rendimento1.setFonte("Salário");
      rendimento1.setValor(5000.0f);
      rendimentoRepository.save(rendimento1);

      Rendimento rendimento2 = new Rendimento();
      rendimento2.setCliente(cliente);
      rendimento2.setFonte("Freelance");
      rendimento2.setValor(1500.0f);
      rendimentoRepository.save(rendimento2);

      // Criar usuário banco
      Usuario usuarioBanco = new Usuario();
      usuarioBanco.setUsername("banco");
      usuarioBanco.setSenha(passwordEncoder.encode("banco"));
      usuarioBanco.setRole("BANCO");
      usuarioRepository.save(usuarioBanco);

      // Criar banco
      Banco banco = new Banco();
      banco.setNome("Banco XYZ");
      banco.setCnpj("12.345.678/0001-90");
      banco.setUsuario(usuarioBanco);
      bancoRepository.save(banco);

      // Criar automóveis
      Automovel automovel1 = new Automovel();
      automovel1.setMatricula("ABC123");
      automovel1.setAno(2020);
      automovel1.setMarca("Toyota");
      automovel1.setModelo("Corolla");
      automovel1.setPlaca("ABC-1234");
      automovel1.setPrecoDiaria(150.0);
      automovel1.setBanco(banco);
      automovelRepository.save(automovel1);

      Automovel automovel2 = new Automovel();
      automovel2.setMatricula("DEF456");
      automovel2.setAno(2021);
      automovel2.setMarca("Honda");
      automovel2.setModelo("Civic");
      automovel2.setPlaca("DEF-5678");
      automovel2.setPrecoDiaria(140.0);
      automovel2.setBanco(banco);
      automovelRepository.save(automovel2);

      Automovel automovel3 = new Automovel();
      automovel3.setMatricula("GHI789");
      automovel3.setAno(2022);
      automovel3.setMarca("Hyundai");
      automovel3.setModelo("HB20");
      automovel3.setPlaca("GHI-9012");
      automovel3.setPrecoDiaria(100.0);
      automovel3.setBanco(banco);
      automovelRepository.save(automovel3);
    };
  }
}