package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import com.raissapayments.conector.domain.dto.commons.InstitucionFinancieraDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class EjecucionRequestDto {
    private Long idSolicitud;
    private Long codigoCliente;
    private String apiKey;
    private List<InstitucionFinancieraDto> listInstituciones;
    private Long idCabeceraEjecucion;

    private LocalDateTime fechaAuditoria;
    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}
