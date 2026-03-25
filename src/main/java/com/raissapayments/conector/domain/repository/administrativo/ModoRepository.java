package com.raissapayments.conector.domain.repository.administrativo;

import com.raissapayments.conector.domain.entity.administrativo.ModoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModoRepository extends JpaRepository<ModoEntity, String> {
}