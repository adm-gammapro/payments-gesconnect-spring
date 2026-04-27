package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.response.TrackingResponseDto;
import com.raissapayments.conector.domain.entity.operativo.TrackingEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TrackingService {
    TrackingEntity crear(Long solicitudId,
                         String evento,
                         String usuario,
                         LocalDateTime fechaCarga,
                         String usuarioAuditoria,
                         String terminalAuditoria,
                         String ipAuditoria);

    List<TrackingResponseDto> listTracking(Long solicitudId);
}