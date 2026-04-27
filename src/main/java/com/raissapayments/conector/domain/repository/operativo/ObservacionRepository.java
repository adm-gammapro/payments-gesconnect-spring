package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.ObservacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacionRepository extends JpaRepository<ObservacionEntity, Long> {
    List<ObservacionEntity> findBySolicitudIdAndEstadoRegistroOrderByIdDesc(Long solicitudId, String estadoRegistro);
}