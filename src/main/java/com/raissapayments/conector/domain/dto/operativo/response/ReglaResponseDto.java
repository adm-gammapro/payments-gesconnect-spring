package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReglaResponseDto {
    private Long codigo;
    private String descripcion;
    private String moneda;
    private BigDecimal limiteInferior;
    private BigDecimal limiteSuperior;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}