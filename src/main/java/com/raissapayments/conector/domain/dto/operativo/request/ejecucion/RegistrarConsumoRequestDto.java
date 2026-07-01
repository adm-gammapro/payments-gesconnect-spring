package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarConsumoRequestDto {
    private String sistemaId;
    private String codigoApi;
    private Long clienteId;
    private String username;
    private Boolean exitoso;
    private String codigoResultado;
    private String observacion;
}