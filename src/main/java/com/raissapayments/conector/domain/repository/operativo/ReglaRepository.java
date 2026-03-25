package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.ReglaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReglaRepository extends JpaRepository<ReglaEntity, Long> {
    Page<ReglaEntity> findByDescripcionContainingIgnoreCase(String desc, Pageable pageable);
}