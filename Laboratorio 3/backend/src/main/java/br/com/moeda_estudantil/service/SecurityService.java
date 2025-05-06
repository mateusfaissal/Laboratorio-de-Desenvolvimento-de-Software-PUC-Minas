package br.com.moeda_estudantil.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.moeda_estudantil.model.Usuario;
import br.com.moeda_estudantil.repository.AlunoRepository;
import br.com.moeda_estudantil.repository.EmpresaParceiraRepository;
import br.com.moeda_estudantil.repository.ResgateCupomRepository;
import br.com.moeda_estudantil.repository.UsuarioRepository;
import br.com.moeda_estudantil.repository.VantagemRepository;

@Service
public class SecurityService {

  private final UsuarioRepository usuarioRepository;
  private final EmpresaParceiraRepository empresaRepository;
  private final VantagemRepository vantagemRepository;
  private final AlunoRepository alunoRepository;
  private final ResgateCupomRepository resgateRepository;

  public SecurityService(UsuarioRepository usuarioRepository,
      EmpresaParceiraRepository empresaRepository,
      VantagemRepository vantagemRepository,
      AlunoRepository alunoRepository,
      ResgateCupomRepository resgateRepository) {
    this.usuarioRepository = usuarioRepository;
    this.empresaRepository = empresaRepository;
    this.vantagemRepository = vantagemRepository;
    this.alunoRepository = alunoRepository;
    this.resgateRepository = resgateRepository;
  }

  /**
   * Verifica se o usuário autenticado é o mesmo do ID fornecido
   */
  public boolean isCurrentUser(String userId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    String email = authentication.getName();
    return usuarioRepository.findById(userId)
        .map(user -> user.getEmail().equals(email))
        .orElse(false);
  }

  /**
   * Verifica se o usuário autenticado é o proprietário da empresa
   */
  public boolean isEmpresaOwner(String empresaId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    String email = authentication.getName();
    return empresaRepository.findById(empresaId)
        .map(empresa -> empresa.getEmail().equals(email))
        .orElse(false);
  }

  /**
   * Verifica se o usuário autenticado é o proprietário da vantagem
   */
  public boolean isVantagemOwner(String vantagemId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    String email = authentication.getName();
    return vantagemRepository.findById(vantagemId)
        .map(vantagem -> vantagem.getEmpresa().getEmail().equals(email))
        .orElse(false);
  }

  /**
   * Verifica se o usuário tem determinado tipo
   */
  public boolean hasUserType(String userId, Usuario.TipoUsuario tipo) {
    return usuarioRepository.findById(userId)
        .map(user -> user.getTipo() == tipo)
        .orElse(false);
  }

  /**
   * Verifica se o usuário é aluno
   */
  public boolean isAluno(String userId) {
    return hasUserType(userId, Usuario.TipoUsuario.ALUNO);
  }

  /**
   * Verifica se o usuário é professor
   */
  public boolean isProfessor(String userId) {
    return hasUserType(userId, Usuario.TipoUsuario.PROFESSOR);
  }

  /**
   * Verifica se o usuário é empresa
   */
  public boolean isEmpresa(String userId) {
    return hasUserType(userId, Usuario.TipoUsuario.EMPRESA);
  }

  /**
   * Verifica se o usuário tem permissão para acessar um resgate
   * Alunos só podem acessar seus próprios resgates
   * Empresas só podem acessar resgates de suas próprias vantagens
   * 
   * @param resgateId ID do resgate
   * @return true se o usuário tem permissão, false caso contrário
   */
  public boolean verificarPermissaoResgate(String resgateId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }

    String email = authentication.getName();

    // Verificar se é um aluno acessando seu próprio resgate
    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_ALUNO")) {
        return resgateRepository.findById(resgateId)
            .map(resgate -> resgate.getAluno().getEmail().equals(email))
            .orElse(false);
      } else if (authority.getAuthority().equals("ROLE_EMPRESA")) {
        return resgateRepository.findById(resgateId)
            .map(resgate -> resgate.getVantagem().getEmpresa().getEmail().equals(email))
            .orElse(false);
      }
    }

    // Para outros tipos de usuário, retorna false
    return false;
  }
}