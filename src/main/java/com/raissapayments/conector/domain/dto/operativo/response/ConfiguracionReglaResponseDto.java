package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class ConfiguracionReglaResponseDto {
    private Long codigo;
    private Long codigoRegla;
    private Long codigoCategoria;
    private String codigoModo;
    private Boolean predeterminado;
    private Integer prioridad;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}