package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.CargaSolicitudJsonRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.CargaSolicitudResponseDto;

public interface CargaSolicitudService {
    CargaSolicitudResponseDto cargarDesdeJson(CargaSolicitudJsonRequestDto req);
}