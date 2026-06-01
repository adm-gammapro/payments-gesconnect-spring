package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Valid
public class CabeceraEjecucionRequestDto {
    private Long codigoJob;

    @Size(max = 3)
    private String codigoSistema;

    private Long codigoCliente;

    private LocalDateTime fechaInicioProceso;

    private LocalDateTime fechaFinProceso;

    @NotBlank
    @Size(max = 20)
    private String estadoProcesamiento;

    private String proceso;

    private String detalleEjecucion;

    @NotNull
    private Integer registrosTotales;

    @NotNull
    private Integer registrosProcesados;

    @NotNull
    private Integer registrosErroneos;

    @NotNull
    private Integer registrosPendientes;

    // Auditoría
    @NotBlank @Size(max = 15)
    private String usuarioAuditoria;

    @NotBlank @Size(max = 30)
    private String terminalAuditoria;

    @NotBlank @Size(max = 20)
    private String ipAuditoria;

    @NotNull
    private LocalDateTime fechaAuditoria;
}