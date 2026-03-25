package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.ObservacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservacionRepository extends JpaRepository<ObservacionEntity, Long> {
}