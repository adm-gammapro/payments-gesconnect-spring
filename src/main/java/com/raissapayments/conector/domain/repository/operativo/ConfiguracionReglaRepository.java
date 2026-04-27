package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.ConfiguracionReglaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ConfiguracionReglaRepository extends JpaRepository<ConfiguracionReglaEntity, Long> {
    @Query("""
            select c
            from ConfiguracionReglaEntity c
            where (:idRegla is null or c.regla.id = :idRegla)
              and (:idCategoria is null or c.categoria.id = :idCategoria)
              and (:modoCodigo is null OR :modoCodigo = '' OR c.modo.codigo = :modoCodigo)
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

    @Query("SELECT c FROM ConfiguracionReglaEntity c " +
            "JOIN FETCH c.regla r " +
            "JOIN FETCH c.categoria cat " +
            "WHERE r.moneda = :moneda " +
            "AND :montoCargo >= r.limiteInferior " +
            "AND :montoCargo < r.limiteSuperior " +
            "AND c.predeterminado IS TRUE " +
            "AND c.estadoRegistro = 'S' " +
            "ORDER BY c.prioridad ASC")
    List<ConfiguracionReglaEntity> findConfiguracionAplicable(
            @Param("moneda") String moneda,
            @Param("montoCargo") BigDecimal montoCargo
    );
}