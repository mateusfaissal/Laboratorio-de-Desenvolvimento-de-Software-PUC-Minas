package br.com.luizcaliza.services;

import br.com.luizcaliza.model.Banco;
import br.com.luizcaliza.model.ContratoDeCredito;
import br.com.luizcaliza.model.Usuario;
import br.com.luizcaliza.repositories.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BancoService {

  @Autowired
  private BancoRepository bancoRepository;

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private ContratoDeCreditoService contratoDeCreditoService;

  public Banco salvar(Banco banco, Usuario usuario) {
    usuario.setRole("BANCO");
    Usuario usuarioSalvo = usuarioService.salvar(usuario);

    banco.setUsuario(usuarioSalvo);
    return bancoRepository.save(banco);
  }

  public Banco atualizar(Banco banco) {
    return bancoRepository.save(banco);
  }

  public Banco buscarPorId(Long id) {
    return bancoRepository.findById(id).orElse(null);
  }

  public List<Banco> listarTodos() {
    return bancoRepository.findAll();
  }

  public ContratoDeCredito gerarContratoCredito(Long bancoId) {
    Banco banco = buscarPorId(bancoId);

    if (banco != null) {
      ContratoDeCredito contrato = new ContratoDeCredito();
      contrato.setBanco(banco);
      return contratoDeCreditoService.salvar(contrato);
    }
    return null;
  }

  public boolean concederContrato(Long bancoId, ContratoDeCredito contrato) {
    Banco banco = buscarPorId(bancoId);

    if (banco != null) {
      contrato.setBanco(banco);
      contratoDeCreditoService.salvar(contrato);
      return true;
    }
    return false;
  }

  public List<ContratoDeCredito> listarContratos(Long bancoId) {
    return contratoDeCreditoService.buscarPorBancoId(bancoId);
  }
}