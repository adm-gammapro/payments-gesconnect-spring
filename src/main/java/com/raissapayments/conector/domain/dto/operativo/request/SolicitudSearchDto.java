package com.raissapayments.conector.domain.dto.operativo.request;

import lombok.Data;

@Data
public class SolicitudSearchDto {
    private String usuario;
    private String fecha;
    private String codigo;
    private String estadoSolicitud;
}