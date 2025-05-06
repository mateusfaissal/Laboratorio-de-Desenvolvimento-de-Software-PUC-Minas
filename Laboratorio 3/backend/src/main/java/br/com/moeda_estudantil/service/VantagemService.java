package br.com.moeda_estudantil.service;

import br.com.moeda_estudantil.exception.RecursoNaoEncontradoException;
import br.com.moeda_estudantil.model.*;
import br.com.moeda_estudantil.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class VantagemService {

  @Value("${app.upload.dir:./uploads}")
  private String uploadDir;

  @Autowired
  private VantagemRepository vantagemRepository;

  @Autowired
  private EmpresaParceiraRepository empresaRepository;

  @Autowired
  private AlunoRepository alunoRepository;

  @Autowired
  private ResgateCupomRepository resgateRepository;

  @Autowired
  private NotificacaoService notificacaoService;

  public List<Vantagem> listarTodas() {
    return vantagemRepository.findAll();
  }

  public List<Vantagem> listarPorEmpresa(String empresaId) {
    return vantagemRepository.findByEmpresaId(empresaId);
  }

  public Vantagem buscarPorId(String id) {
    return vantagemRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vantagem não encontrada"));
  }

  @Transactional
  public Vantagem cadastrar(Vantagem vantagem, String empresaId) {
    EmpresaParceira empresa = empresaRepository.findById(empresaId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Empresa parceira não encontrada"));

    vantagem.setEmpresa(empresa);
    return vantagemRepository.save(vantagem);
  }

  @Transactional
  public Vantagem atualizar(String id, Vantagem vantagemAtualizada) {
    Vantagem vantagemExistente = vantagemRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vantagem não encontrada"));

    // Atualizar dados da vantagem
    vantagemExistente.setNome(vantagemAtualizada.getNome());
    vantagemExistente.setDescricao(vantagemAtualizada.getDescricao());
    vantagemExistente.setCustoMoedas(vantagemAtualizada.getCustoMoedas());

    // Não permitimos alterar a empresa ou a foto aqui

    return vantagemRepository.save(vantagemExistente);
  }

  @Transactional
  public void deletar(String id) {
    if (!vantagemRepository.existsById(id)) {
      throw new RecursoNaoEncontradoException("Vantagem não encontrada");
    }

    // Verificar se há resgates desta vantagem
    if (vantagemRepository.hasResgateCupons(id)) {
      throw new IllegalStateException(
          "Não é possível excluir a vantagem porque ela possui resgates vinculados");
    }

    Vantagem vantagem = vantagemRepository.findById(id).get();

    // Se houver foto, excluir o arquivo
    if (vantagem.getFotoUrl() != null && !vantagem.getFotoUrl().isEmpty()) {
      try {
        Path fotoPath = Paths.get(vantagem.getFotoUrl());
        if (Files.exists(fotoPath)) {
          Files.delete(fotoPath);
        }
      } catch (IOException e) {
        // Log do erro, não interrompendo o fluxo principal
        System.err.println("Erro ao excluir arquivo de foto: " + e.getMessage());
      }
    }

    vantagemRepository.deleteById(id);
  }

  @Transactional
  public Vantagem adicionarFoto(String id, MultipartFile foto) {
    Vantagem vantagem = vantagemRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vantagem não encontrada"));

    // Verificar se o arquivo é uma imagem
    if (!foto.getContentType().startsWith("image/")) {
      throw new IllegalArgumentException("O arquivo deve ser uma imagem");
    }

    // Criar diretório de upload se não existir
    File uploadDirFile = new File(uploadDir);
    if (!uploadDirFile.exists()) {
      uploadDirFile.mkdirs();
    }

    // Gerar nome único para o arquivo
    String originalFilename = foto.getOriginalFilename();
    String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    String newFilename = "vantagem_" + id + "_" + UUID.randomUUID().toString() + extension;
    Path targetPath = Paths.get(uploadDir, newFilename);

    try {
      // Salvar o arquivo
      Files.copy(foto.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

      // Excluir a foto antiga se existir
      if (vantagem.getFotoUrl() != null && !vantagem.getFotoUrl().isEmpty()) {
        Path oldFotoPath = Paths.get(vantagem.getFotoUrl());
        if (Files.exists(oldFotoPath)) {
          Files.delete(oldFotoPath);
        }
      }

      // Atualizar a URL da foto na vantagem
      vantagem.setFotoUrl(targetPath.toString());

      return vantagemRepository.save(vantagem);

    } catch (IOException e) {
      throw new RuntimeException("Erro ao salvar a foto: " + e.getMessage());
    }
  }

  @Transactional
  public ResgateCupom resgatar(String vantagemId, String email) {
    // Buscar a vantagem
    Vantagem vantagem = vantagemRepository.findById(vantagemId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Vantagem não encontrada"));

    // Buscar o aluno
    Aluno aluno = alunoRepository.findByEmail(email)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado"));

    // Verificar se o aluno tem saldo suficiente
    Double custoMoedas = vantagem.getCustoMoedas();
    if (aluno.getSaldoMoedas() < custoMoedas) {
      throw new IllegalArgumentException(
          "Saldo insuficiente. Saldo atual: " + aluno.getSaldoMoedas() + " | Custo: " + custoMoedas);
    }

    // Criar o resgate
    ResgateCupom resgate = new ResgateCupom();
    resgate.setAluno(aluno);
    resgate.setVantagem(vantagem);
    resgate.setValor(custoMoedas);
    resgate.setDescricao("Resgate de \"" + vantagem.getNome() + "\" por " + aluno.getNome());

    // Debitar o valor do saldo do aluno
    aluno.setSaldoMoedas(aluno.getSaldoMoedas() - custoMoedas);
    alunoRepository.save(aluno);

    // Salvar o resgate
    ResgateCupom resgateSalvo = resgateRepository.save(resgate);

    // Enviar notificações
    notificacaoService.notificarResgateCupom(resgateSalvo);

    return resgateSalvo;
  }
}