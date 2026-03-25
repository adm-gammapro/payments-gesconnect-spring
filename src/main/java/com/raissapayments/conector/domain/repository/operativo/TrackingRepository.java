package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.TrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackingRepository extends JpaRepository<TrackingEntity, Long> {
}