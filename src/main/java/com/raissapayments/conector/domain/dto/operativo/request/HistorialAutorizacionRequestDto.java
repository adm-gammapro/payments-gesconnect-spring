package com.raissapayments.conector.domain.dto.operativo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Valid
public class HistorialAutorizacionRequestDto {
    @NotNull
    private Long solicitudId;

    @NotNull
    private BigDecimal limiteInferior;

    @NotNull
    private BigDecimal limiteSuperior;

    @NotBlank
    @Size(max = 3)
    private String moneda;

    @NotNull
    private Long categoriaId;

    @NotBlank @Size(max = 50)
    private String descripcionCategoria;

    @NotBlank @Size(max = 50)
    private String usuarioAutorizador;

    @NotNull
    private LocalDateTime fechaAutorizacion;

    @NotNull
    private LocalDateTime fechaAuditoria;

    @NotBlank @Size(max = 15)
    private String usuarioAuditoria;

    @NotBlank @Size(max = 30)
    private String terminalAuditoria;

    @NotBlank @Size(max = 20)
    private String ipAuditoria;
}