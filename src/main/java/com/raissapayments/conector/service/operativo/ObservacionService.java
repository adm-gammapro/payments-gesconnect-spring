package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ObservacionResponseDto;

public interface ObservacionService {
    ObservacionResponseDto crearObservacion(ObservacionRequestDto request);
}