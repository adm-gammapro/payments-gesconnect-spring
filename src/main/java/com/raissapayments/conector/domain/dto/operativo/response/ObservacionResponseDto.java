package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObservacionResponseDto {
    private Long id;
    private String descripcion;
    private String tipoObservacion;
    private String eventoObservacion;
    private String usuarioObservacion;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
    private Long solicitudId;
}