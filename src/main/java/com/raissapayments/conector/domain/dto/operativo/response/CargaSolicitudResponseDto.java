package com.raissapayments.conector.domain.dto.operativo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CargaSolicitudResponseDto {
    Long solicitudId;
    int cargosCreados;
    int abonosCreados;
    int observacionesRegistradas;
    String estadoSolicitud;
}