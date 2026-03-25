package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonosSolicitudRepository extends JpaRepository<AbonosSolicitudEntity, Long> {
}