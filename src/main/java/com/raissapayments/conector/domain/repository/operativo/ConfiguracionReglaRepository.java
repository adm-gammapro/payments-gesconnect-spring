package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.ConfiguracionReglaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConfiguracionReglaRepository extends JpaRepository<ConfiguracionReglaEntity, Long> {
    @Query("""
            select c
            from ConfiguracionReglaEntity c
            where (:idRegla is null or c.regla.id = :idRegla)
              and (:idCategoria is null or c.categoria.id = :idCategoria)
              and (:modoCodigo is null or c.modo.codigo = :modoCodigo)
              and (:estado is null or c.estadoRegistro = :estado)
            order by c.id asc, c.regla.id asc, c.prioridad asc
            """)
    Page<ConfiguracionReglaEntity> search(
            @Param("idRegla") Long idRegla,
            @Param("idCategoria") Long idCategoria,
            @Param("modoCodigo") String modoCodigo,
            @Param("estado") String estado,
            Pageable pageable
    );
}