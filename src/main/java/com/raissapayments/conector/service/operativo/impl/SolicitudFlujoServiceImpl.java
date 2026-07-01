package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.CambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionCambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.EjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.CabeceraEjecucionResponseDto;
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
import com.raissapayments.conector.exception.operativo.ErrorControladoException;
import com.raissapayments.conector.service.operativo.CabeceraEjecucionService;
import com.raissapayments.conector.service.operativo.EjecucionService;
import com.raissapayments.conector.service.operativo.ObservacionService;
import com.raissapayments.conector.service.operativo.SolicitudFlujoService;
import com.raissapayments.conector.service.operativo.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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
    private final CabeceraEjecucionService cabeceraEjecucionService;

    @Override
    @Transactional
    public Long validar(CambioEstadoRequestDto req) throws Exception {
        int registrosTotales = 0;
        int registrosProcesados = 0;
        int registrosPendientes;
        int registrosErroneos = 0;

        List<CargoSolicitudEntity> listCargo = cargoSolicitudRepo.findBySolicitudIdAndEstadoRegistro(req.getSolicitudId(),
                                                                                                Constante.ESTADO_ACTIVO);
        List<String> cuentasNoConcuerdan = new ArrayList<>();
        List<String> monedasCuentasNoConcuerdan = new ArrayList<>();
        CabeceraEjecucionRequestDto cabeceraEjecucion = new CabeceraEjecucionRequestDto();
        CabeceraEjecucionResponseDto responseCabeceraEjecucion = new CabeceraEjecucionResponseDto();

        for (CargoSolicitudEntity cargo : listCargo) {
            registrosTotales = cargo.getAbonos().size();
            registrosPendientes = cargo.getAbonos().size();

            cabeceraEjecucion.setCodigoJob(req.getSolicitudId());
            cabeceraEjecucion.setCodigoCliente(req.getCodigoCliente());
            cabeceraEjecucion.setCodigoSistema(Constante.SISTEMA_PAYMENTS);
            cabeceraEjecucion.setFechaInicioProceso(LocalDateTime.now());
            cabeceraEjecucion.setRegistrosTotales(registrosTotales);
            cabeceraEjecucion.setRegistrosProcesados(registrosProcesados);
            cabeceraEjecucion.setRegistrosPendientes(registrosPendientes);
            cabeceraEjecucion.setRegistrosErroneos(registrosErroneos);
            cabeceraEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_PROCESANDO);
            cabeceraEjecucion.setProceso(Constante.PROCESO_VALIDACION);
            cabeceraEjecucion.setDetalleEjecucion("Iniciando validación");
            cabeceraEjecucion.setFechaAuditoria(LocalDateTime.now());
            cabeceraEjecucion.setUsuarioAuditoria(req.getUsuarioAuditoria());
            cabeceraEjecucion.setIpAuditoria(req.getIpAuditoria());
            cabeceraEjecucion.setTerminalAuditoria(req.getTerminalAuditoria());
            responseCabeceraEjecucion = cabeceraEjecucionService.registrar(cabeceraEjecucion);

            String cuentaOrigen = cargo.getCuentaOrigen();
            if (cuentaOrigen == null || cuentaOrigen.isBlank()) {
                registrosErroneos = registrosTotales;

                cabeceraEjecucion = new CabeceraEjecucionRequestDto();
                cabeceraEjecucion.setFechaFinProceso(LocalDateTime.now());
                cabeceraEjecucion.setRegistrosTotales(registrosTotales);
                cabeceraEjecucion.setRegistrosProcesados(0);
                cabeceraEjecucion.setRegistrosPendientes(0);
                cabeceraEjecucion.setRegistrosErroneos(registrosErroneos);
                cabeceraEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO_ERROR);
                cabeceraEjecucion.setProceso(Constante.PROCESO_VALIDACION);
                cabeceraEjecucion.setDetalleEjecucion("El campo cuentaOrigen no puede ser nulo o vacío en CargoSolicitud con id: " + cargo.getId());
                cabeceraEjecucion.setFechaAuditoria(LocalDateTime.now());
                cabeceraEjecucion.setUsuarioAuditoria(req.getUsuarioAuditoria());
                cabeceraEjecucion.setIpAuditoria(req.getIpAuditoria());
                cabeceraEjecucion.setTerminalAuditoria(req.getTerminalAuditoria());
                cabeceraEjecucionService.actualizar(responseCabeceraEjecucion.getId(), cabeceraEjecucion);

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
            ejecucion.setIdCabeceraEjecucion(responseCabeceraEjecucion.getId());
            ejecucion.setListInstituciones(req.getListInstituciones());
            ejecucion.setUsuarioAuditoria(req.getUsuarioAuditoria());
            ejecucion.setFechaAuditoria(LocalDateTime.now());
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
            registrosErroneos = registrosTotales;

            cabeceraEjecucion = new CabeceraEjecucionRequestDto();
            cabeceraEjecucion.setFechaFinProceso(LocalDateTime.now());
            cabeceraEjecucion.setRegistrosTotales(registrosTotales);
            cabeceraEjecucion.setRegistrosProcesados(0);
            cabeceraEjecucion.setRegistrosPendientes(0);
            cabeceraEjecucion.setRegistrosErroneos(registrosErroneos);
            cabeceraEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO_ERROR);
            cabeceraEjecucion.setProceso(Constante.PROCESO_VALIDACION);
            String truncado = observacionConcatenada.length() > 2000 ? observacionConcatenada.substring(0, 2000) : observacionConcatenada;
            cabeceraEjecucion.setDetalleEjecucion(truncado);
            cabeceraEjecucion.setFechaAuditoria(LocalDateTime.now());
            cabeceraEjecucion.setUsuarioAuditoria(req.getUsuarioAuditoria());
            cabeceraEjecucion.setIpAuditoria(req.getIpAuditoria());
            cabeceraEjecucion.setTerminalAuditoria(req.getTerminalAuditoria());
            cabeceraEjecucionService.actualizar(responseCabeceraEjecucion.getId(), cabeceraEjecucion);

            ObservacionCambioEstadoRequestDto observacion = cargarDatosObservacion(req,
                    Constante.EVENTO_VALIDAR,
                    observacionConcatenada);

            registrarObservacion(observacion, Constante.TIPO_OBSERVACION_OBSERVADO);
            cambiarEstado(req, Constante.ESTADO_SOLICITUD_OBSERVADO, Constante.EVENTO_VALIDAR);
        }

        idSolicitud = this.enProcesamiento(req);

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
        String estadoSolicitud = gestionAutorizacion(req);
        return cambiarEstado(req, estadoSolicitud, Constante.EVENTO_AUTORIZAR);
    }

    @Override
    @Transactional
    public Long ejecutar(CambioEstadoRequestDto req) throws Exception {
        Long idSolicitud;
        EjecucionRequestDto ejecucion = new EjecucionRequestDto();

        ejecucion.setIdSolicitud(req.getSolicitudId());
        ejecucion.setCodigoCliente(req.getCodigoCliente());
        ejecucion.setListInstituciones(req.getListInstituciones());
        ejecucion.setUsuarioAuditoria(req.getUsuarioAuditoria());
        ejecucion.setIpAuditoria(req.getIpAuditoria());
        ejecucion.setTerminalAuditoria(req.getTerminalAuditoria());
        EjecucionResponseDto responseConsulta = ejecucionService.confirmarTransferenciaInmediata(ejecucion);

        if(responseConsulta.getStatus().equals(Constante.KEY_ERROR_CODE)) {
            ObservacionCambioEstadoRequestDto observacion = cargarDatosObservacion(req,
                    Constante.EVENTO_EJECUTAR,
                    responseConsulta.getMessage());
            registrarObservacion(observacion, Constante.TIPO_OBSERVACION_OBSERVADO);

            cambiarEstado(req, Constante.ESTADO_SOLICITUD_PROCESADO_PARCIAL, Constante.EVENTO_EJECUTAR);
        } else {
            cambiarEstado(req, Constante.ESTADO_SOLICITUD_PROCESADO_TOTAL, Constante.EVENTO_EJECUTAR);
        }

        idSolicitud = this.enProcesamiento(req);

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

    @Override
    @Transactional
    public Long enProcesamiento(CambioEstadoRequestDto req) {
        if (req == null) throw new IllegalArgumentException("Request obligatorio");
        SolicitudEntity sol = solicitudRepo.findById(req.getSolicitudId())
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada: " + req.getSolicitudId()));

        boolean enProcesamiento = !sol.getEnProcesamiento();

        sol.setEnProcesamiento(enProcesamiento);
        sol.setAudiFechaMod(LocalDateTime.now());
        sol.setAudiUsuMod(req.getUsuarioAuditoria());
        sol.setAudiIpMod(req.getIpAuditoria());
        sol.setAudiNomTerminalMod(req.getTerminalAuditoria());

        solicitudRepo.save(sol);

        return sol.getId();
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
        oreq.setFechaAuditoria(LocalDateTime.now());
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
                                gestionAutorizacionSolicitud.setAudiFechIns(LocalDateTime.now());
                                gestionAutorizacionSolicitud.setAudiIp(req.getIpAuditoria());
                                gestionAutorizacionSolicitud.setAudiNomTerminal(req.getTerminalAuditoria());
                                gestionAutorizacionSolicitudRepository.save(gestionAutorizacionSolicitud);
                            }
                        } else {
                            idSolicitud = -2L;//No encuentra configuraciones
                        }
                    }
                } else {
                    idSolicitud = -1L;//No hay configuraciones en base a reglas
                }
            }
        } else {
            idSolicitud = 0L;//No hay solicitud
        }

        return idSolicitud;
    }

    private String gestionAutorizacion(CambioEstadoRequestDto req) {
        String estadosolicitud;

        int actualizados = gestionAutorizacionSolicitudRepository
                .actualizarAutorizacionesPendientes(req.getSolicitudId(),
                        req.getUsuarioAuditoria(),
                        LocalDateTime.now(),
                        req.getIpAuditoria(),
                        req.getTerminalAuditoria()
                );

        if (actualizados == 0) {
            throw new ErrorControladoException(
                    String.format("No se encontraron autorizaciones pendientes para la solicitud: %d",
                            req.getSolicitudId())
            );
        } else {
            List<GestionAutorizacionSolicitudEntity> newGestiones = gestionAutorizacionSolicitudRepository
                    .obtenerListaAutorizacionesPorPrioridad(req.getSolicitudId(),
                            "PENDIENTE",
                            Constante.ESTADO_ACTIVO);
            if(!newGestiones.isEmpty()) {
                estadosolicitud = Constante.ESTADO_SOLICITUD_AUTORIZADO_PARCIAL;
            } else {
                estadosolicitud = Constante.ESTADO_SOLICITUD_AUTORIZADO;
            }
        }

        log.debug("Actualizadas {} autorizaciones para solicitud: {}", actualizados, req.getSolicitudId());

        return estadosolicitud;
    }
}