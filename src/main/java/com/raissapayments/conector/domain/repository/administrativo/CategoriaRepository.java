package com.raissapayments.conector.domain.repository.administrativo;

import com.raissapayments.conector.domain.entity.administrativo.CategoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    Page<CategoriaEntity> findByDescripcionContainingIgnoreCase(String desc, Pageable pageable);
}
