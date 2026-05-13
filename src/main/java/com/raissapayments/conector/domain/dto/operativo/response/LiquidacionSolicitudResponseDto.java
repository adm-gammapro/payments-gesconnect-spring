package com.raissapayments.conector.domain.dto.operativo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class LiquidacionSolicitudResponseDto {
    private Long idSolicitud;
    private BigDecimal totalLiquidacion;
    private BigDecimal cargo;
    private BigDecimal totalCobros;
    private BigDecimal totalComisiones;
    private BigDecimal totalImpuestos;
    private BigDecimal totalComisionesOrigen;
    private BigDecimal totalComisionesDestino;
    private String moneda;
    List<DetalleLiquidacionSolicitud> detalle;
}
