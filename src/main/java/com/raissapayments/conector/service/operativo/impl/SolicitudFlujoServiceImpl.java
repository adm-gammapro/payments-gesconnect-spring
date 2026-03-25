package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.CambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionCambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.entity.commons.EstadoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.repository.commons.EstadoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.service.operativo.ObservacionService;
import com.raissapayments.conector.service.operativo.SolicitudFlujoService;
import com.raissapayments.conector.service.operativo.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SolicitudFlujoServiceImpl implements SolicitudFlujoService {
    private final SolicitudRepository solicitudRepo;
    private final EstadoSolicitudRepository estadoRepo;
    private final TrackingService trackingService;
    private final ObservacionService observacionService;

    @Override
    @Transactional
    public void validar(CambioEstadoRequestDto req) {
        cambiarEstado(req, Constante.ESTADO_SOLICITUD_VALIDADO, "VALIDAR");
    }

    @Override
    @Transactional
    public void enviarAutorizacion(CambioEstadoRequestDto req) {
        cambiarEstado(req, Constante.ESTADO_SOLICITUD_PENDIENTE_AUTORIZACION, "ENVIAR_AUTORIZACION");
    }

    @Override
    @Transactional
    public void autorizar(CambioEstadoRequestDto req) {
        cambiarEstado(req, Constante.ESTADO_SOLICITUD_AUTORIZADO, "AUTORIZAR");
    }

    @Override
    @Transactional
    public void ejecutar(CambioEstadoRequestDto req) {
        cambiarEstado(req, Constante.ESTADO_SOLICITUD_PROCESADO_TOTAL, "EJECUTAR");
    }

    @Override
    @Transactional
    public void observar(ObservacionCambioEstadoRequestDto req) {
        cambiarEstado(req, Constante.ESTADO_SOLICITUD_OBSERVADO, "OBSERVAR");
        registrarObservacion(req, Constante.TIPO_OBSERVACION_OBSERVADO);
    }

    @Override
    @Transactional
    public void anular(ObservacionCambioEstadoRequestDto req) {
        cambiarEstado(req, Constante.ESTADO_SOLICITUD_ANULADO, "ANULAR");
        registrarObservacion(req, Constante.TIPO_OBSERVACION_ANULADO);
    }

    /**
     * Cambia el estado de la solicitud
     * @param req Datos de la solicitud
     * @param codigoEstado Codigo de estado a cambiar
     * @param eventoTracking Codigo del evento lanzado
     */
    private void cambiarEstado(CambioEstadoRequestDto req, String codigoEstado, String eventoTracking) {
        if (req == null) throw new IllegalArgumentException("Request obligatorio");
        SolicitudEntity sol = solicitudRepo.findById(req.getSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + req.getSolicitudId()));

        EstadoSolicitudEntity estado = estadoRepo.findById(codigoEstado)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + codigoEstado));

        sol.setEstadoSolicitud(estado);
        sol.setAudiFechaMod(LocalDateTime.now());
        sol.setAudiUsuMod(req.getUsuarioAuditoria());
        sol.setAudiIpMod(req.getIpAuditoria());
        sol.setAudiNomTerminalMod(req.getTerminalAuditoria());

        solicitudRepo.save(sol);

        trackingService.crear(
                sol.getId(),
                eventoTracking,
                req.getUsuario(),
                LocalDateTime.now(),
                req.getUsuarioAuditoria(),
                req.getTerminalAuditoria(),
                req.getIpAuditoria()
        );
    }

    private void registrarObservacion(ObservacionCambioEstadoRequestDto req, String tipoObs) {
        ObservacionRequestDto oreq = new ObservacionRequestDto();
        oreq.setSolicitudId(req.getSolicitudId());
        oreq.setDescripcion(req.getDescripcionObservacion());
        oreq.setTipoObservacion(tipoObs);
        oreq.setFechaAuditoria(req.getFechaAuditoria());
        oreq.setUsuarioAuditoria(req.getUsuarioAuditoria());
        oreq.setTerminalAuditoria(req.getTerminalAuditoria());
        oreq.setIpAuditoria(req.getIpAuditoria());
        observacionService.crearObservacion(oreq);
    }
}