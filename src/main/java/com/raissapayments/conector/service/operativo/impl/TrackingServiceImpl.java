package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.TrackingEntity;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.TrackingRepository;
import com.raissapayments.conector.service.operativo.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {
    private final TrackingRepository trackingRepository;
    private final SolicitudRepository solicitudRepository;

    @Override
    @Transactional
    public TrackingEntity crear(Long solicitudId,
                                String evento,
                                String usuario,
                                LocalDateTime fechaCarga,
                                String usuarioAuditoria,
                                String terminalAuditoria,
                                String ipAuditoria) {
        if (solicitudId == null) throw new IllegalArgumentException("El id de solicitud es obligatorio");
        if (isBlank(evento)) throw new IllegalArgumentException("El evento es obligatorio");
        if (isBlank(usuario)) throw new IllegalArgumentException("El usuario es obligatorio");
        if (fechaCarga == null) throw new IllegalArgumentException("La fecha de carga es obligatoria");
        if (isBlank(usuarioAuditoria)) throw new IllegalArgumentException("usuarioAuditoria es obligatorio");
        if (isBlank(terminalAuditoria)) throw new IllegalArgumentException("terminalAuditoria es obligatorio");
        if (isBlank(ipAuditoria)) throw new IllegalArgumentException("ipAuditoria es obligatorio");

        SolicitudEntity solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + solicitudId));

        TrackingEntity tracking = TrackingEntity.builder()
                .solicitud(solicitud)
                .evento(evento.trim())
                .usuario(usuario.trim())
                .fechaCarga(fechaCarga)
                .build();

        tracking.setEstadoRegistro(Constante.ESTADO_ACTIVO);
        tracking.setAudiFechIns(LocalDateTime.now());
        tracking.setAudiUsuario(usuarioAuditoria.trim());
        tracking.setAudiNomTerminal(terminalAuditoria.trim());
        tracking.setAudiIp(ipAuditoria.trim());

        return trackingRepository.save(tracking);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}