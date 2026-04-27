package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.GestionAutorizacionSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GestionAutorizacionSolicitudRepository extends JpaRepository<GestionAutorizacionSolicitudEntity, Long> {
    List<GestionAutorizacionSolicitudEntity> findByCodigoSolicitudAndEstadoProcesamientoAndEstadoRegistro(Long codigoSolicitud,
                                                                                                        String estadoProcesamiento,
                                                                                                        String estadoRegistro);
}