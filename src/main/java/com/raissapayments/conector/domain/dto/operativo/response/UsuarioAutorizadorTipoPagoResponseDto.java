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
public class UsuarioAutorizadorTipoPagoResponseDto {
    private Long id;
    private String username;
    private Long tipoPagoId;
    private String tipoPagoNombre; // ajusta al campo real en TipoPago
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}