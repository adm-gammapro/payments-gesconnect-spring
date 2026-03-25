package com.raissapayments.conector.domain.dto.administrativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

@Data
public class ModoResponseDto {
    private String codigo;
    private String descripcion;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}