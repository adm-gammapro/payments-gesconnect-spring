package com.raissapayments.conector.domain.repository.administrativo;

import com.raissapayments.conector.domain.entity.administrativo.TipoPagoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TipoPagoRepository extends JpaRepository<TipoPagoEntity, Long> {
    @Query("SELECT t FROM TipoPagoEntity t " +
            "WHERE (:descripcion IS NULL OR :descripcion = '' OR LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) " +
            "AND (:estadoRegistro IS NULL OR t.estadoRegistro = :estadoRegistro) ")
    Page<TipoPagoEntity> findByDescripcionContainingIgnoreCaseAndEstadoRegistro(
            @Param("descripcion") String descripcion,
            @Param("estadoRegistro") String estadoRegistro,
            Pageable pageable);

    List<TipoPagoEntity> findByEstadoRegistro(String estadoRegistro);
}