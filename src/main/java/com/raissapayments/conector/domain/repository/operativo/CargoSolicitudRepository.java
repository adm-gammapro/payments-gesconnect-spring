package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CargoSolicitudRepository extends JpaRepository<CargoSolicitudEntity, Long> {
    List<CargoSolicitudEntity> findBySolicitudIdAndEstadoRegistro(Long solicitudId, String estadoRegistro);
}