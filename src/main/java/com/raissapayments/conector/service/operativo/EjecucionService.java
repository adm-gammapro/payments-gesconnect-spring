package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.EjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.EjecucionResponseDto;

public interface EjecucionService {
    EjecucionResponseDto consultarTransferenciaInmediata(EjecucionRequestDto request);

    EjecucionResponseDto confirmarTransferenciaInmediata(EjecucionRequestDto request);
}
