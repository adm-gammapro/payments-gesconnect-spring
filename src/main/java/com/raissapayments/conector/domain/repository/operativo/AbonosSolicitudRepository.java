package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AbonosSolicitudRepository extends JpaRepository<AbonosSolicitudEntity, Long> {
    @Query("SELECT a FROM AbonosSolicitudEntity a " +
            "WHERE a.cargoSolicitud.id = :idCargoSolicitud " +
            "AND a.estadoRegistro = :estadoRegistro " +
            "AND (a.estadoEjecucionConsulta IS NULL " +
            "OR a.estadoEjecucionConsulta != 'OK')")
    List<AbonosSolicitudEntity> findAbonosPendientesConsultaByCargo(@Param("idCargoSolicitud") Long idCargoSolicitud,
                                                                    @Param("estadoRegistro") String estadoRegistro);

    @Query("SELECT a FROM AbonosSolicitudEntity a " +
            "WHERE a.cargoSolicitud.id = :idCargoSolicitud " +
            "AND a.estadoRegistro = :estadoRegistro " +
            "AND (a.estadoEjecucionTransferencia IS NULL " +
            "OR a.estadoEjecucionTransferencia != 'OK')")
    List<AbonosSolicitudEntity> findAbonosPendientesConfirmacionByCargo(@Param("idCargoSolicitud") Long idCargoSolicitud,
                                                                        @Param("estadoRegistro") String estadoRegistro);

    AbonosSolicitudEntity findByIdAndEstadoRegistro(Long id, String estadoRegistro);
}