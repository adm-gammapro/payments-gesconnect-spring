package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.HistorialAutorizacionRequestDto;
import com.raissapayments.conector.domain.entity.operativo.HistorialAutorizacionEntity;

public interface HistorialAutorizacionService {
    HistorialAutorizacionEntity crear(HistorialAutorizacionRequestDto request);
}