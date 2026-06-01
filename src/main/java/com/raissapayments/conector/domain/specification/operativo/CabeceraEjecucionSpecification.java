package com.raissapayments.conector.domain.specification.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionSearchDto;
import com.raissapayments.conector.domain.entity.operativo.CabeceraEjecucionEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CabeceraEjecucionSpecification {
    private CabeceraEjecucionSpecification() {}

    public static Specification<CabeceraEjecucionEntity> conFiltros(
            CabeceraEjecucionSearchDto filtros) {

        return Specification
                .where(porEstadoRegistro(Constante.ESTADO_ACTIVO))
                .and(porEstadoProcesamiento(filtros.getEstadoProcesamiento()))
                .and(porRangoFechaInicio(filtros.getFechaInicial(), filtros.getFechaFinal()));
    }

    private static Specification<CabeceraEjecucionEntity> porEstadoRegistro(
            String estadoRegistro) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(estadoRegistro)) return null;
            return cb.equal(root.get("estadoRegistro"), estadoRegistro);
        };
    }

    private static Specification<CabeceraEjecucionEntity> porEstadoProcesamiento(
            String estadoProcesamiento) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(estadoProcesamiento)) return null;
            return cb.equal(root.get("estadoProcesamiento"), estadoProcesamiento);
        };
    }

    private static Specification<CabeceraEjecucionEntity> porRangoFechaInicio(
            String fechaInicial, String fechaFinal) {
        return (root, query, cb) -> {
            boolean tieneInicio = StringUtils.hasText(fechaInicial);
            boolean tieneFin    = StringUtils.hasText(fechaFinal);

            if (!tieneInicio && !tieneFin) return null;

            LocalDateTime inicio = tieneInicio
                    ? LocalDate.parse(fechaInicial, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .atStartOfDay()
                    : LocalDateTime.MIN;

            LocalDateTime fin = tieneFin
                    ? LocalDate.parse(fechaFinal, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    .atTime(LocalTime.MAX)
                    : LocalDateTime.MAX;

            return cb.between(root.get("fechaInicioProceso"), inicio, fin);
        };
    }
}
