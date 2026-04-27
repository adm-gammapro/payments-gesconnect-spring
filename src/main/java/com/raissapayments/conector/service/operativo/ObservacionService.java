package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ObservacionResponseDto;

import java.util.List;

public interface ObservacionService {
    ObservacionResponseDto crearObservacion(ObservacionRequestDto request);

    List<ObservacionResponseDto> listarObservacion(ObservacionRequestDto request);
}