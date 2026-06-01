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
public class DetalleEjecucionRequestDto {
    @NotNull
    private Long codigoCabeceraEjecucion;

    private Long codigoCuenta;

    @NotBlank
    @Size(max = 30)
    private String numeroCuenta;

    @NotBlank
    @Size(max = 3)
    private String codigoEntidadFinanciera;

    @NotBlank
    @Size(max = 100)
    private String nombreEntidadFinanciera;

    @NotBlank
    @Size(max = 3)
    private String monedaCuenta;

    @NotBlank
    @Size(max = 30)
    private String descripcionMoneda;

    @NotBlank
    @Size(max = 20)
    private String estadoProcesamiento;

    @Size(max = 2000)
    private String detalleEjecucion;

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