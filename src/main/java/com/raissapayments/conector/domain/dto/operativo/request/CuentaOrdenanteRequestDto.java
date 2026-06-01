package com.raissapayments.conector.domain.dto.operativo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaOrdenanteRequestDto {
    private Long codigo;

    @NotBlank(message = "El usuario ordenante es obligatorio")
    private String usuarioOrdenante;

    private String passwordOrdenante;

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuentaOrdenante;

    @NotBlank(message = "La moneda es obligatoria")
    private String monedaCuentaOrdenante;

    private String tipoDocumentoOrdenante;

    private String documentoOrdenante;

    @NotBlank(message = "El nombre del ordenante es obligatorio")
    private String nombreOrdenante;

    private String apellidoPaternoOrdenante;

    private String apellidoMaternoOrdenante;

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
