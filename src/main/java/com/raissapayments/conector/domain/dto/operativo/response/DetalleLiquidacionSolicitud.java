package com.raissapayments.conector.domain.dto.operativo.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class DetalleLiquidacionSolicitud {
    private String cciDestino;
    private String cliente;
    private String moneda;
    private BigDecimal monto;
    private BigDecimal itf;
    private BigDecimal comisionOrigen;
    private BigDecimal comisionDestino;
}
