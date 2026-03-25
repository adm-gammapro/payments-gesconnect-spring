package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AbonoSolicitudResponseDto {
    private Long id;
    private Long cargoSolicitudId;
    private String cuentaDestino;
    private String codigoEntidadFinanciera;
    private String moneda;
    private BigDecimal montoDestino;
    private String beneficiario;
    private String estadoEjecucion;
    private String detalleEjecucion;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}