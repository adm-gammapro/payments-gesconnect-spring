package com.raissapayments.conector.domain.repository.commons;

import com.raissapayments.conector.domain.entity.commons.EstadoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoSolicitudRepository extends JpaRepository<EstadoSolicitudEntity, String> {
}
