package com.raissapayments.conector.domain.repository.administrativo;

import com.raissapayments.conector.domain.entity.administrativo.TipoPagoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPagoRepository extends JpaRepository<TipoPagoEntity, Long> {
    Page<TipoPagoEntity> findByDescripcionContainingIgnoreCase(String desc, Pageable pageable);
}
