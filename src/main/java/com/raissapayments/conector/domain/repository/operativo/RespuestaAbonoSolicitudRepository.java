package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.RespuestaAbonoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespuestaAbonoSolicitudRepository extends JpaRepository<RespuestaAbonoSolicitudEntity, Long> {
    RespuestaAbonoSolicitudEntity findByCodigoAbonoSolicitudAndEstadoRegistro(Long codigoAbonoSolicitud, String estadoRegistro);

    List<RespuestaAbonoSolicitudEntity> findByCodigoAbonoSolicitudInAndEstadoRegistro(List<Long> idsAbonos, String estadoRegistro);
}