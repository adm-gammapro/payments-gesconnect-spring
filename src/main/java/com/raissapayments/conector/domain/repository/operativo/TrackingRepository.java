package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.TrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingRepository extends JpaRepository<TrackingEntity, Long> {
    List<TrackingEntity> findBySolicitudIdAndEstadoRegistroOrderByIdDesc(Long solicitudId, String estadoRegistro);
}