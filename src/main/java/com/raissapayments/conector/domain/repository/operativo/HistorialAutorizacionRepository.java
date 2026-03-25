package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.HistorialAutorizacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialAutorizacionRepository extends JpaRepository<HistorialAutorizacionEntity, Long> {
}