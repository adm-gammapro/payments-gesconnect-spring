package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class ConfiguracionReglaResponseDto {
    private Long codigo;
    private Long codigoRegla;
    private String descripcionRegla;
    private Long codigoCategoria;
    private String descripcionCategoria;
    private String codigoModo;
    private String descripcionModo;
    private String predeterminado;
    private Integer prioridad;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}