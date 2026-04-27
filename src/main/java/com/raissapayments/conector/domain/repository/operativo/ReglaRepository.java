package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.ReglaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReglaRepository extends JpaRepository<ReglaEntity, Long> {
    @Query("SELECT t FROM ReglaEntity t " +
            "WHERE (:descripcion IS NULL OR :descripcion = '' OR LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) " +
            "AND (:moneda IS NULL OR :moneda = '' OR LOWER(t.moneda) LIKE LOWER(CONCAT('%', :moneda, '%'))) " +
            "AND (:estadoRegistro IS NULL OR t.estadoRegistro = :estadoRegistro) ")
    Page<ReglaEntity> findByDescripcionContainingIgnoreCaseAndMonedaContainingIgnoreCaseAndEstadoRegistro(
            @Param("descripcion") String descripcion,
            @Param("moneda") String moneda,
            @Param("estadoRegistro") String estadoRegistro,
            Pageable pageable);

    List<ReglaEntity> findByEstadoRegistro(String estadoRegistro);
}