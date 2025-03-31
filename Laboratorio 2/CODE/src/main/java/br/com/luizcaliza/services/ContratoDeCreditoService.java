package br.com.luizcaliza.services;

import br.com.luizcaliza.model.Banco;
import br.com.luizcaliza.model.ContratoDeCredito;
import br.com.luizcaliza.repositories.ContratoDeCreditoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratoDeCreditoService {

  @Autowired
  private ContratoDeCreditoRepository contratoCreditoRepository;

  @Autowired
  private BancoService bancoService;

  public ContratoDeCredito salvar(ContratoDeCredito contrato) {
    return contratoCreditoRepository.save(contrato);
  }

  public ContratoDeCredito atualizar(ContratoDeCredito contrato) {
    return contratoCreditoRepository.save(contrato);
  }

  public ContratoDeCredito buscarPorId(Long id) {
    return contratoCreditoRepository.findById(id).orElse(null);
  }

  public List<ContratoDeCredito> listarTodos() {
    return contratoCreditoRepository.findAll();
  }

  public List<ContratoDeCredito> buscarPorBancoId(Long bancoId) {
    return contratoCreditoRepository.findByBancoId(bancoId);
  }

  public boolean definirBanco(Long contratoId, Long bancoId) {
    ContratoDeCredito contrato = buscarPorId(contratoId);
    Banco banco = bancoService.buscarPorId(bancoId);

    if (contrato != null && banco != null) {
      contrato.setBanco(banco);
      contratoCreditoRepository.save(contrato);
      return true;
    }
    return false;
  }

  public boolean validarContrato(Long contratoId) {
    ContratoDeCredito contrato = buscarPorId(contratoId);

    if (contrato != null) {
      contrato.setAtivo(true);
      contratoCreditoRepository.save(contrato);
      return true;
    }
    return false;
  }
}