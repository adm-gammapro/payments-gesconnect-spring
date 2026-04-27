package com.raissapayments.conector.domain.repository.administrativo;

import com.raissapayments.conector.domain.entity.administrativo.CategoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
    @Query("SELECT t FROM CategoriaEntity t " +
            "WHERE (:descripcion IS NULL OR :descripcion = '' OR LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%'))) " +
            "AND (:estadoRegistro IS NULL OR t.estadoRegistro = :estadoRegistro) ")
    Page<CategoriaEntity> findByDescripcionContainingIgnoreCaseAndEstadoRegistro(
            @Param("descripcion") String descripcion,
            @Param("estadoRegistro") String estadoRegistro,
            Pageable pageable);

    List<CategoriaEntity> findByEstadoRegistroOrderById(String estadoRegistro);
}
