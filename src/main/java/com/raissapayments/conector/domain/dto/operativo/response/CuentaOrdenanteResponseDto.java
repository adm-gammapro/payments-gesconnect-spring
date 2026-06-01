package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaOrdenanteResponseDto {
    private Long codigo;
    private String usuarioOrdenante;
    private String passwordOrdenante;
    private String numeroCuentaOrdenante;
    private String monedaCuentaOrdenante;
    private String tipoDocumentoOrdenante;
    private String documentoOrdenante;
    private String nombreOrdenante;
    private String apellidoPaternoOrdenante;
    private String apellidoMaternoOrdenante;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}