package com.raissapayments.conector.domain.specification.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionSearchDto;
import com.raissapayments.conector.domain.entity.operativo.DetalleEjecucionEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class DetalleEjecucionSpecification {
    private DetalleEjecucionSpecification() {}

    public static Specification<DetalleEjecucionEntity> conFiltros(
            DetalleEjecucionSearchDto filtros) {

        return Specification
                .where(porCabecera(filtros.getCodigoCabeceraEjecucion()))
                .and(porEstadoRegistroActivo(Constante.ESTADO_ACTIVO));
    }

    private static Specification<DetalleEjecucionEntity> porCabecera(
            Long codigoCabecera) {
        return (root, query, cb) -> {
            if (codigoCabecera == null) return null;
            return cb.equal(root.get("codigoCabeceraEjecucion"), codigoCabecera);
        };
    }

    private static Specification<DetalleEjecucionEntity> porEstadoRegistroActivo(
            String estadoRegistro) {
        return (root, query, cb) -> {
            // si no se envía, por defecto filtra solo activos
            String valor = StringUtils.hasText(estadoRegistro) ? estadoRegistro : "S";
            return cb.equal(root.get("estadoRegistro"), valor);
        };
    }
}
