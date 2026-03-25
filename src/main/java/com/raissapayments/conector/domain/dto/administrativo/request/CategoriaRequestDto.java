package com.raissapayments.conector.domain.dto.administrativo.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoriaRequestDto {
    private Long codigo;
    private String descripcion;

    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
