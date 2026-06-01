package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.DetalleEjecucionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DetalleEjecucionRepository extends JpaRepository<DetalleEjecucionEntity, Long>,
        JpaSpecificationExecutor<DetalleEjecucionEntity> {
}
