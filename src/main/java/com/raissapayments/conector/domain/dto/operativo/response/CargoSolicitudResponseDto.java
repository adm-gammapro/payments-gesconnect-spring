package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CargoSolicitudResponseDto {
    private Long id;
    private Long solicitudId;
    private String cuentaOrigen;
    private String codigoEntidadFinanciera;
    private String moneda;
    private BigDecimal montoCargo;
    private BigDecimal montoTotalAbonos;
    private String estadoValidacion;
    private String estadoEjecucion;
    private List<AbonoSolicitudResponseDto> abonos;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}
