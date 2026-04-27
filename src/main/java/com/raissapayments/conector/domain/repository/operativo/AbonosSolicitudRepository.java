package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbonosSolicitudRepository extends JpaRepository<AbonosSolicitudEntity, Long> {
    List<AbonosSolicitudEntity> findByCargoSolicitudIdAndEstadoRegistro(Long idCargosolicitud, String estadoRegistro);
}