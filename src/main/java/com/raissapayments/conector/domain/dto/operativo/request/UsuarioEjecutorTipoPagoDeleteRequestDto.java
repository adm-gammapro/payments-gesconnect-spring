package com.raissapayments.conector.domain.dto.operativo.request;

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
public class UsuarioEjecutorTipoPagoDeleteRequestDto {
    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime fechaAuditoria;

    @NotBlank
    @Size(max = 15)
    private String usuarioAuditoria;

    @NotBlank @Size(max = 30)
    private String terminalAuditoria;

    @NotBlank @Size(max = 20)
    private String ipAuditoria;
}