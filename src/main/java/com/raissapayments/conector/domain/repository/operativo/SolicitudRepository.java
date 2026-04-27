package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long>, JpaSpecificationExecutor<SolicitudEntity> {
    SolicitudEntity findByIdAndEstadoRegistro(Long id, String estadoRegistro);
}