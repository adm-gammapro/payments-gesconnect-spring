package com.raissapayments.conector.domain.dto.operativo.request;

import com.raissapayments.conector.domain.dto.commons.InstitucionFinancieraDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambioEstadoRequestDto {
    @NotNull
    private Long solicitudId;

    @NotBlank
    private String usuario;

    private List<InstitucionFinancieraDto> listInstituciones;

    @NotNull
    private Long codigoCliente;

    @NotBlank
    @Size(max = 15)
    private String usuarioAuditoria;

    @NotBlank
    @Size(max = 30)
    private String terminalAuditoria;

    @NotBlank
    @Size(max = 20)
    private String ipAuditoria;
}