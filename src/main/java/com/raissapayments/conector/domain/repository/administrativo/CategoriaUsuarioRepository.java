package com.raissapayments.conector.domain.repository.administrativo;

import com.raissapayments.conector.domain.entity.administrativo.CategoriaUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CategoriaUsuarioRepository extends JpaRepository<CategoriaUsuarioEntity, Long> {
    List<CategoriaUsuarioEntity> findByCategoriaIdAndEstadoRegistro(Long idCategoria, String estadoRegistro);

    Optional<CategoriaUsuarioEntity> findByCategoriaIdAndUsername(Long categoriaId, String username);

    List<CategoriaUsuarioEntity> findByCategoriaIdAndUsernameIn(Long categoriaId, List<String> usernames);

    @Modifying
    @Query("""
                UPDATE CategoriaUsuarioEntity c
                SET c.estadoRegistro = 'N',
                            c.audiUsuMod = :usuarioAuditoria,
                            c.audiFechaMod = :fechaAuditoria,
                            c.audiIpMod = :ipAuditoria,
                            c.audiNomTerminalMod = :terAuditoria
                WHERE c.categoria.id = :categoriaId
                  AND c.username IN :usernames
            """)
    int desvincularUsuarios(@Param("categoriaId") Long categoriaId,
                            @Param("usernames") List<String> usernames,
                            @Param("usuarioAuditoria") String usuarioAuditoria,
                            @Param("fechaAuditoria") LocalDateTime fechaAuditoria,
                            @Param("ipAuditoria") String ipAuditoria,
                            @Param("terAuditoria") String terAuditoria);
}