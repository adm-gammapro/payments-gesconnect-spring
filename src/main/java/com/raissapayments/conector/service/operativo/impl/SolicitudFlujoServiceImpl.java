package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.CambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.EjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionCambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.EjecucionResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.CategoriaUsuarioEntity;
import com.raissapayments.conector.domain.entity.commons.EstadoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.ConfiguracionReglaEntity;
import com.raissapayments.conector.domain.entity.operativo.CuentaOrdenanteEntity;
import com.raissapayments.conector.domain.entity.operativo.GestionAutorizacionSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.repository.administrativo.CategoriaUsuarioRepository;
import com.raissapayments.conector.domain.repository.commons.EstadoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.CargoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.ConfiguracionReglaRepository;
import com.raissapayments.conector.domain.repository.operativo.CuentaOrdenanteRepository;
import com.raissapayments.conector.domain.repository.operativo.GestionAutorizacionSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.service.operativo.EjecucionService;
import com.raissapayments.conector.service.operativo.ObservacionService;
import com.raissapayments.conector.service.operativo.SolicitudFlujoService;
import com.raissapayments.conector.service.operativo.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudFlujoServiceImpl implements SolicitudFlujoService {
    private final SolicitudRepository solicitudRepo;
    private final CargoSolicitudRepository cargoSolicitudRepo;
    private final EstadoSolicitudRepository estadoRepo;
    private final TrackingService trackingService;
    private final ObservacionService observacionService;
    private final CuentaOrdenanteRepository cuentaOrdenanteRepository;
    private final EjecucionService ejecucionService;
    private final GestionAutorizacionSolicitudRepository gestionAutorizacionSolicitudRepository;
    private final ConfiguracionReglaRepository configuracionReglaRepository;
    private final CategoriaUsuarioRepository categoriausuarioRepository;

    @Override
    @Transactional
    public Long validar(CambioEstadoRequestDto req) {
        List<CargoSolicitudEntity> listCargo = cargoSolicitudRepo.findBySolicitudIdAndEstadoRegistro(req.getSolicitudId(),
                                                                                                Constante.ESTADO_ACTIVO);
        List<String> cuentasNoConcuerdan = new ArrayList<>();
        List<String> monedasCuentasNoConcuerdan = new ArrayList<>();

        for (CargoSolicitudEntity cargo : listCargo) {
            String cuentaOrigen = cargo.getCuentaOrigen();
            if (cuentaOrigen == null || cuentaOrigen.isBlank()) {
                throw new IllegalArgumentException("El campo cuentaOrigen no puede ser nulo o vacío en CargoSolicitud con id: " + cargo.getId());
            }

            CuentaOrdenanteEntity cuentaOpt = cuentaOrdenanteRepository.findByNumeroCuentaOrdenanteAndEstadoRegistro(
                    cuentaOrigen,
                    Constante.ESTADO_ACTIVO
            );

            if (cuentaOpt == null) {
                cuentasNoConcuerdan.add(cuentaOrigen);
            }
            if (cuentaOpt != null && !cuentaOpt.getMonedaCuentaOrdenante().equals(cargo.getMoneda())) {
                    monedasCuentasNoConcuerdan.add("Cuenta " + cargo.getCuentaOrigen() + " con moneda " + cargo.getMoneda());
            }

        }

        String observacionConcatenada = "";
        if (!monedasCuentasNoConcuerdan.isEmpty()) {
            observacionConcatenada = "Moneda de cuenta origen que no concuerdan con la moneda de la cuenta originante registrada: " + String.join(", ", monedasCuentasNoConcuerdan);
        }

        if (!cuentasNoConcuerdan.isEmpty()) {
            if (!monedasCuentasNoConcuerdan.isEmpty()) {
                observacionConcatenada = observacionConcatenada + " | ";
            }
            observacionConcatenada = observacionConcatenada + "Cuentas de origen que no concuerdan con la cuenta originante registrada: " + String.join(", ", cuentasNoConcuerdan);
        }

        Long idSolicitud;
        if (observacionConcatenada.isEmpty()) {
            EjecucionRequestDto ejecucion = new EjecucionRequestDto();
            ejecucion.setIdSolicitud(req.getSolicitudId());
            ejecucion.setListInstituciones(req.getListInstituciones());
            ejecucion.setUsuarioAuditoria(req.getUsuarioAuditoria());
            ejecucion.setFechaAuditoria(req.getFechaAuditoria());
            ejecucion.setIpAuditoria(req.getIpAuditoria());
            ejecucion.setTerminalAuditoria(req.getTerminalAuditoria());
            EjecucionResponseDto responseConsulta = ejecucionService.consultarTransferenciaInmediata(ejecucion);
            if(responseConsulta.getStatus().equals(Constante.KEY_ERROR_CODE)) {
                ObservacionCambioEstadoRequestDto observacion = cargarDatosObservacion(req,
                        Constante.EVENTO_VALIDAR,
                        responseConsulta.getMessage());
                registrarObservacion(observacion, Constante.TIPO_OBSERVACION_OBSERVADO);

                cambiarEstado(req, Constante.ESTADO_SOLICITUD_OBSERVADO, Constante.EVENTO_VALIDAR);
                idSolicitud = 0L;
            } else if (responseConsulta.getStatus().equals(Constante.KEY_NOT_ACTION_CODE)) {
                idSolicitud = -1L;
            } else {
                idSolicitud = cambiarEstado(req, Constante.ESTADO_SOLICITUD_VALIDADO, Constante.EVENTO_VALIDAR);
            }
        } else {
            ObservacionCambioEstadoRequestDto observacion = cargarDatosObservacion(req,
                    Constante.EVENTO_VALIDAR,
                    observacionConcatenada);

            registrarObservacion(observacion, Constante.TIPO_OBSERVACION_OBSERVADO);
            idSolicitud = cambiarEstado(req, Constante.ESTADO_SOLICITUD_OBSERVADO, Constante.EVENTO_VALIDAR);
        }
        return idSolicitud;
    }

    @Override
    @Transactional
    public Long enviarAutorizacion(CambioEstadoRequestDto req) {
        Long idSolicitud = aplicarConfiguracionAutorizacion(req);
        if(idSolicitud > 0L) {
            return cambiarEstado(req, Constante.ESTADO_SOLICITUD_PENDIENTE_AUTORIZACION, Constante.EVENTO_ENVIAR_AUTORIZACION);
        }
        return idSolicitud;
    }

    @Override
    @Transactional
    public Long autorizar(CambioEstadoRequestDto req) {
        return cambiarEstado(req, Constante.ESTADO_SOLICITUD_AUTORIZADO, "AUTORIZAR");
    }

    @Override
    @Transactional
    public Long ejecutar(CambioEstadoRequestDto req) {
        Long idSolicitud;
        EjecucionRequestDto ejecucion = new EjecucionRequestDto();
        ejecucion.setIdSolicitud(req.getSolicitudId());
        EjecucionResponseDto responseConsulta = ejecucionService.confirmarTransferenciaInmediata(ejecucion);

        if(responseConsulta.getStatus().equals(Constante.KEY_ERROR_CODE)) {
            ObservacionCambioEstadoRequestDto observacion = cargarDatosObservacion(req,
                    Constante.EVENTO_EJECUTAR,
                    responseConsulta.getMessage());
            registrarObservacion(observacion, Constante.TIPO_OBSERVACION_OBSERVADO);

            idSolicitud = cambiarEstado(req, Constante.ESTADO_SOLICITUD_PROCESADO_PARCIAL, Constante.EVENTO_EJECUTAR);
        } else {
            idSolicitud = cambiarEstado(req, Constante.ESTADO_SOLICITUD_PROCESADO_TOTAL, Constante.EVENTO_EJECUTAR);
        }

        return idSolicitud;
    }

    @Override
    @Transactional
    public Long observar(ObservacionCambioEstadoRequestDto req) {
        Long idSolicitud = cambiarEstado(req, Constante.ESTADO_SOLICITUD_OBSERVADO, "OBSERVAR");
        registrarObservacion(req, Constante.TIPO_OBSERVACION_OBSERVADO);

        return idSolicitud;
    }

    @Override
    @Transactional
    public Long anular(ObservacionCambioEstadoRequestDto req) {
        Long idSolicitud = cambiarEstado(req, Constante.ESTADO_SOLICITUD_ANULADO, "ANULAR");
        registrarObservacion(req, Constante.TIPO_OBSERVACION_ANULADO);

        return idSolicitud;
    }

    /**
     * Cambia el estado de la solicitud
     * @param req Datos de la solicitud
     * @param codigoEstado Codigo de estado a cambiar
     * @param eventoTracking Codigo del evento lanzado
     */
    private Long cambiarEstado(CambioEstadoRequestDto req, String codigoEstado, String eventoTracking) {
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

        return sol.getId();
    }

    private void registrarObservacion(ObservacionCambioEstadoRequestDto req, String tipoObs) {
        ObservacionRequestDto oreq = new ObservacionRequestDto();
        oreq.setSolicitudId(req.getSolicitudId());
        oreq.setDescripcion(req.getDescripcionObservacion());
        oreq.setTipoObservacion(tipoObs);
        oreq.setEventoObservacion(req.getEventoObservacion());
        oreq.setUsuarioObservacion(req.getUsuarioObservacion());
        oreq.setFechaAuditoria(req.getFechaAuditoria());
        oreq.setUsuarioAuditoria(req.getUsuarioAuditoria());
        oreq.setTerminalAuditoria(req.getTerminalAuditoria());
        oreq.setIpAuditoria(req.getIpAuditoria());
        observacionService.crearObservacion(oreq);
    }

    private ObservacionCambioEstadoRequestDto cargarDatosObservacion(CambioEstadoRequestDto req,
                                                                     String evento,
                                                                     String descripcionObervacion) {
        ObservacionCambioEstadoRequestDto observacion = new ObservacionCambioEstadoRequestDto();
        observacion.setSolicitudId(req.getSolicitudId());
        observacion.setUsuario(req.getUsuario());
        observacion.setUsuarioObservacion(req.getUsuario());
        observacion.setEventoObservacion(evento);
        observacion.setUsuarioAuditoria(req.getUsuarioAuditoria());
        observacion.setFechaAuditoria(req.getFechaAuditoria());
        observacion.setIpAuditoria(req.getIpAuditoria());
        observacion.setTerminalAuditoria(req.getTerminalAuditoria());
        observacion.setDescripcionObservacion(descripcionObervacion);

        return observacion;
    }

    private Long aplicarConfiguracionAutorizacion(CambioEstadoRequestDto req) {
        Long idSolicitud = req.getSolicitudId();
        SolicitudEntity solicitud = solicitudRepo.findByIdAndEstadoRegistro(req.getSolicitudId(), Constante.ESTADO_ACTIVO);
        if (solicitud != null) {
            for (CargoSolicitudEntity cargo : solicitud.getCargos()) {
                List<ConfiguracionReglaEntity> configsAplicables =
                        configuracionReglaRepository.findConfiguracionAplicable(
                                cargo.getMoneda(),
                                cargo.getMontoCargo()
                        );

                if (!configsAplicables.isEmpty()) {
                    for (ConfiguracionReglaEntity config : configsAplicables) {
                        List<CategoriaUsuarioEntity> listCategoriaUsuario = categoriausuarioRepository.findByCategoriaIdAndEstadoRegistro(config.getCategoria().getId(),
                                Constante.ESTADO_ACTIVO);
                        if (!listCategoriaUsuario.isEmpty()) {
                            for (CategoriaUsuarioEntity categoriaUsuario : listCategoriaUsuario) {
                                GestionAutorizacionSolicitudEntity gestionAutorizacionSolicitud = new GestionAutorizacionSolicitudEntity();
                                gestionAutorizacionSolicitud.setCodigoSolicitud(req.getSolicitudId());
                                gestionAutorizacionSolicitud.setUsername(categoriaUsuario.getUsername());
                                gestionAutorizacionSolicitud.setPrioridad(config.getPrioridad());
                                gestionAutorizacionSolicitud.setEstadoProcesamiento(Constante.ESTADO_GESTION_AUTORIZACION_PENDIENTE);
                                gestionAutorizacionSolicitud.setEstadoRegistro(Constante.ESTADO_ACTIVO);
                                gestionAutorizacionSolicitud.setAudiUsuario(req.getUsuarioAuditoria());
                                gestionAutorizacionSolicitud.setAudiFechIns(req.getFechaAuditoria());
                                gestionAutorizacionSolicitud.setAudiIp(req.getIpAuditoria());
                                gestionAutorizacionSolicitud.setAudiNomTerminal(req.getTerminalAuditoria());
                                gestionAutorizacionSolicitudRepository.save(gestionAutorizacionSolicitud);
                            }
                        } else {
                            idSolicitud = -2L;
                        }
                    }
                } else {
                    idSolicitud = -1L;
                }
            }
        } else {
            idSolicitud = 0L;
        }

        return idSolicitud;
    }
}