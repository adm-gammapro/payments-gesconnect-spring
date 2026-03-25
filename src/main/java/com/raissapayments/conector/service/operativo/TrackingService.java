package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.entity.operativo.TrackingEntity;

import java.time.LocalDateTime;

public interface TrackingService {
    TrackingEntity crear(Long solicitudId,
                         String evento,
                         String usuario,
                         LocalDateTime fechaCarga,
                         String usuarioAuditoria,
                         String terminalAuditoria,
                         String ipAuditoria);
}