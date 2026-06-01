package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long>, JpaSpecificationExecutor<SolicitudEntity> {
    SolicitudEntity findByIdAndEstadoRegistro(Long id, String estadoRegistro);

    @Query("""
            SELECT DISTINCT s
            FROM SolicitudEntity s
            WHERE s.estadoRegistro = 'S'
              AND EXISTS (
                  SELECT g FROM GestionAutorizacionSolicitudEntity g
                  WHERE g.codigoSolicitud = s.id
                    AND g.username = :usuarioActual
                    AND g.estadoProcesamiento = 'PENDIENTE'
                    AND g.estadoRegistro = 'S'
                    AND g.prioridad = (
                        SELECT MIN(g2.prioridad)
                        FROM GestionAutorizacionSolicitudEntity g2
                        WHERE g2.codigoSolicitud = s.id
                          AND g2.estadoProcesamiento = 'PENDIENTE'
                          AND g2.estadoRegistro = 'S'
                    )
              )
              AND (:codigo IS NULL OR s.id = :codigo)
              AND (:usuarios IS NULL OR s.usuarioCarga IN :usuarios)
              AND (:estadosSolicitud IS NULL OR s.estadoSolicitud.codigo IN :estadosSolicitud)
              AND (CAST(:fechaInicio AS date) IS NULL OR CAST(:fechaFin AS date) IS NULL OR
                   s.fechaCarga BETWEEN :fechaInicio AND :fechaFin)
            """)
    Page<SolicitudEntity> findSolicitudesPendientesParaAutorizar(@Param("usuarioActual") String usuarioActual,
                                                                 @Param("codigo") String codigo,
                                                                 @Param("usuarios") List<String> usuarios,
                                                                 @Param("estadosSolicitud") List<String> estadosSolicitud,
                                                                 @Param("fechaInicio") LocalDateTime fechaInicio,
                                                                 @Param("fechaFin") LocalDateTime fechaFin,
                                                                 Pageable pageable
    );

    @Query("""
        SELECT DISTINCT s
        FROM SolicitudEntity s
        WHERE s.estadoRegistro = 'S'
          AND (:usuarios IS NULL OR s.usuarioCarga IN :usuarios)
          AND (:codigo IS NULL OR s.id = :codigo)
          AND (:estadosSolicitud IS NULL OR s.estadoSolicitud.codigo IN :estadosSolicitud)
              AND (CAST(:fechaInicio AS date) IS NULL OR CAST(:fechaFin AS date) IS NULL OR
                   s.fechaCarga BETWEEN :fechaInicio AND :fechaFin)
          AND (:usuarioActual IS NULL OR :usuarioActual = '' OR s.usuarioCarga = :usuarioActual)
        ORDER BY s.fechaCarga DESC
        """)
    Page<SolicitudEntity> findSolicitudesByFiltrosCargar(
            @Param("usuarios") List<String> usuarios,
            @Param("codigo") String codigo,
            @Param("estadosSolicitud") List<String> estadosSolicitud,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            @Param("usuarioActual") String usuarioActual,
            Pageable pageable);

    @Query("""
            SELECT DISTINCT s
            FROM SolicitudEntity s
            WHERE s.estadoRegistro = 'S'
              AND (:usuarios IS NULL OR s.usuarioCarga IN :usuarios)
              AND (:codigo IS NULL OR s.id = :codigo)
              AND (:estadosSolicitud IS NULL OR s.estadoSolicitud.codigo IN :estadosSolicitud)
              AND (CAST(:fechaInicio AS date) IS NULL OR CAST(:fechaFin AS date) IS NULL OR
                   s.fechaCarga BETWEEN :fechaInicio AND :fechaFin)
            """)
    Page<SolicitudEntity> findSolicitudesByFiltros(@Param("usuarios") List<String> usuarios,
                                                   @Param("codigo") String codigo,
                                                   @Param("estadosSolicitud") List<String> estadosSolicitud,
                                                   @Param("fechaInicio") LocalDateTime fechaInicio,
                                                   @Param("fechaFin") LocalDateTime fechaFin,
                                                   Pageable pageable);
}