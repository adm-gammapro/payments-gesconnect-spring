package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.CabeceraEjecucionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CabeceraEjecucionRepository extends JpaRepository<CabeceraEjecucionEntity, Long>,
        JpaSpecificationExecutor<CabeceraEjecucionEntity> {
}
