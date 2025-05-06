package br.com.moeda_estudantil.repository;

import br.com.moeda_estudantil.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, String> {
  List<Notificacao> findByDestinatario(String destinatario);

  List<Notificacao> findByStatus(Notificacao.StatusNotificacao status);

  List<Notificacao> findByResgateId(String resgateId);

  @Query("SELECT n FROM Notificacao n WHERE n.dataEnvio BETWEEN :inicio AND :fim")
  List<Notificacao> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

  @Query("SELECT n FROM Notificacao n WHERE n.status = br.com.moeda_estudantil.model.Notificacao.StatusNotificacao.PENDENTE ORDER BY n.dataEnvio ASC")
  List<Notificacao> findPendentes();
}