package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoSolicitudRepository extends JpaRepository<CargoSolicitudEntity, Long> {
}