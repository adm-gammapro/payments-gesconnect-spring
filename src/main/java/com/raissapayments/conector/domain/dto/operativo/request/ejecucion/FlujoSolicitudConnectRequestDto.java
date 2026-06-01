package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import com.raissapayments.conector.domain.dto.commons.InstitucionFinancieraDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlujoSolicitudConnectRequestDto {
    private Long solicitudId;
    private String usuario;
    private List<InstitucionFinancieraDto> listInstituciones;
    private Long codigoCliente;

    private String usuarioAuditoria;
    private String terminalAuditoria;
    private String ipAuditoria;
}