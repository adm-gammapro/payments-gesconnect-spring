package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.GestionAutorizacionSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GestionAutorizacionSolicitudRepository extends JpaRepository<GestionAutorizacionSolicitudEntity, Long> {
    @Query("SELECT g FROM GestionAutorizacionSolicitudEntity g WHERE " +
            "g.codigoSolicitud = :codigoSolicitud AND " +
            "g.estadoProcesamiento = :estadoProcesamiento AND " +
            "g.estadoRegistro = :estadoRegistro AND " +
            "g.prioridad = (SELECT MIN(g2.prioridad) FROM GestionAutorizacionSolicitudEntity g2 " +
            "               WHERE g2.codigoSolicitud = :codigoSolicitud " +
            "               AND g2.estadoProcesamiento = :estadoProcesamiento " +
            "               AND g2.estadoRegistro = :estadoRegistro)")
    List<GestionAutorizacionSolicitudEntity> obtenerListaAutorizacionesPorPrioridad(@Param("codigoSolicitud") Long codigoSolicitud,
                                                                                    @Param("estadoProcesamiento") String estadoProcesamiento,
                                                                                    @Param("estadoRegistro") String estadoRegistro);

    @Modifying
    @Query("UPDATE GestionAutorizacionSolicitudEntity g SET " +
            "g.estadoProcesamiento = 'PROCESADO', " +
            "g.audiUsuMod = :usuarioMod, " +
            "g.audiFechaMod = :fechaMod, " +
            "g.audiIpMod = :ipMod, " +
            "g.audiNomTerminalMod = :terminalMod " +
            "WHERE g.codigoSolicitud = :solicitudId " +
            "AND g.estadoProcesamiento = 'PENDIENTE' " +
            "AND g.estadoRegistro = 'S' " +
            "AND g.prioridad = (" +
            "    SELECT MIN(g2.prioridad) " +
            "    FROM GestionAutorizacionSolicitudEntity g2 " +
            "    WHERE g2.codigoSolicitud = :solicitudId " +
            "    AND g2.estadoProcesamiento = 'PENDIENTE' " +
            "    AND g2.estadoRegistro = 'S'" +
            ")")
    int actualizarAutorizacionesPendientes(@Param("solicitudId") Long solicitudId,
                                           @Param("usuarioMod") String usuarioMod,
                                           @Param("fechaMod") LocalDateTime fechaMod,
                                           @Param("ipMod") String ipMod,
                                           @Param("terminalMod") String terminalMod);
}