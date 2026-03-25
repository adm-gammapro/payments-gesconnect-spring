package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.CargaSolicitudJsonRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.LineaCargaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.CargaSolicitudResponseDto;
import com.raissapayments.conector.domain.entity.commons.EstadoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.ObservacionEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.mapper.operativo.ObservacionMapper;
import com.raissapayments.conector.domain.repository.commons.EstadoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.AbonosSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.CargoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.ObservacionRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.service.operativo.CargaSolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CargaSolicitudServiceImpl implements CargaSolicitudService {
    private final SolicitudRepository solicitudRepo;
    private final CargoSolicitudRepository cargoRepo;
    private final AbonosSolicitudRepository abonoRepo;
    private final ObservacionRepository obsRepo;
    private final EstadoSolicitudRepository estadoSolicitudRepo;
    private final ObservacionMapper observacionMapper;

    @Override
    @Transactional
    public CargaSolicitudResponseDto cargarDesdeJson(CargaSolicitudJsonRequestDto req) {
        EstadoSolicitudEntity estadoRegistrado = estadoSolicitudRepo.findById(Constante.ESTADO_SOLICITUD_REGISTRADO)
                .orElseThrow(() -> new IllegalStateException("Estado solicitud no registrado"));

        SolicitudEntity solicitud = new SolicitudEntity();
        solicitud.setFechaCarga(LocalDateTime.now());
        solicitud.setUsuarioCarga(req.getUsuarioCarga());
        solicitud.setEstadoSolicitud(estadoRegistrado);

        ParseResult parseResult = parseLineas(req.getLineas(),
                req.getUsuarioAuditoria(), req.getTerminalAuditoria(), req.getIpAuditoria());

        solicitud.setCantidadOrdenes(parseResult.cargos.size());
        solicitud.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());
        solicitud.setAudiUsuario(req.getUsuarioAuditoria());
        solicitud.setAudiFechIns(req.getFechaAuditoria());
        solicitud.setAudiNomTerminal(req.getTerminalAuditoria());
        solicitud.setAudiIp(req.getIpAuditoria());
        SolicitudEntity finalSolicitud = solicitudRepo.save(solicitud);

        persistirResultados(finalSolicitud,
                            parseResult);

        return new CargaSolicitudResponseDto(
                finalSolicitud.getId(),
                parseResult.cargos.size(),
                parseResult.abonos.size(),
                parseResult.observaciones.size(),
                estadoRegistrado.getCodigo()
        );
    }

    private record ParseResult(
            List<CargoSolicitudEntity> cargos,
            List<AbonosSolicitudEntity> abonos,
            List<ObservacionEntity> observaciones
    ) {}

    private void validarCargoYAbonos(CargoSolicitudEntity cargo,
                                     List<AbonosSolicitudEntity> abonos,
                                     List<ObservacionEntity> observaciones,
                                     String usuarioAuditoria,
                                     String terminalAuditoria,
                                     String ipAuditoria) {
        if (abonos.isEmpty()) {
            observaciones.add(obs("El cargo con cuenta origen " + cargo.getCuentaOrigen() +
                    " no tiene filas D asociadas",
                    usuarioAuditoria,
                    terminalAuditoria,
                    ipAuditoria));
            cargo.setEstadoValidacion(Constante.ESTADO_VALIDACION_OBSERVADO);
            return;
        }

        BigDecimal montoCargo = safe(cargo.getMontoCargo()).setScale(2, RoundingMode.HALF_UP);

        BigDecimal sumaAbonos = abonos.stream()
                .map(a -> safe(a.getMontoDestino()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        if (montoCargo.compareTo(sumaAbonos) != 0) {
            observaciones.add(obs(
                    "Descuadre de montos para cuenta origen " + cargo.getCuentaOrigen() +
                            ": monto H=" + montoCargo.toPlainString() + ", suma D=" + sumaAbonos.toPlainString(),
                    usuarioAuditoria,
                    terminalAuditoria,
                    ipAuditoria));
            cargo.setEstadoValidacion(Constante.ESTADO_VALIDACION_OBSERVADO);
        }

        boolean monedaMismatch = abonos.stream()
                .anyMatch(a -> !cargo.getMoneda().equalsIgnoreCase(a.getMoneda()));
        if (monedaMismatch) {
            observaciones.add(obs("Moneda de abonos difiere de la cabecera para cuenta origen " +
                    cargo.getCuentaOrigen(),
                    usuarioAuditoria,
                    terminalAuditoria,
                    ipAuditoria));
            cargo.setEstadoValidacion(Constante.ESTADO_VALIDACION_OBSERVADO);
        }
    }

    private ObservacionEntity obs(String descripcion,
                                  String usuarioAuditoria,
                                  String terminalAuditoria,
                                  String ipAuditoria) {
        ObservacionRequestDto obs = new ObservacionRequestDto();
        obs.setDescripcion(descripcion);
        obs.setTipoObservacion(Constante.TIPO_OBSERVACION_OBSERVADO);
        obs.setFechaAuditoria(LocalDateTime.now());
        obs.setUsuarioAuditoria(usuarioAuditoria);
        obs.setTerminalAuditoria(terminalAuditoria);
        obs.setIpAuditoria(ipAuditoria);

        return observacionMapper.requestDtoToEntity(obs);
    }

    private ParseResult parseLineas(List<LineaCargaRequestDto> lineas,
                                    String usuarioAuditoria,
                                    String terminalAuditoria,
                                    String ipAuditoria) {
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("No hay líneas para procesar");
        }

        List<CargoSolicitudEntity> cargos = new ArrayList<>();
        List<AbonosSolicitudEntity> abonos = new ArrayList<>();
        List<ObservacionEntity> observaciones = new ArrayList<>();

        CargoSolicitudEntity cargoActual = null;
        List<AbonosSolicitudEntity> abonosActuales = new ArrayList<>();
        boolean hayAlMenosUnH = false;

        for (int i = 0; i < lineas.size(); i++) {
            LineaCargaRequestDto l = lineas.get(i);
            String tipo = safe(l.getTipo());
            String cuenta = safe(l.getCuenta());
            String codEnt = safe(l.getCodigoEntidadFinanciera());
            String moneda = safe(l.getMoneda());
            BigDecimal monto = safe(l.getMonto());

            // Validaciones de vacíos
            if (isEmpty(tipo)) observaciones.add(obs(Constante.CONSTANTE_VALOR_LINEA + (i+1) + ": campo Tipo vacío", usuarioAuditoria, terminalAuditoria, ipAuditoria));
            if (isEmpty(cuenta)) observaciones.add(obs(Constante.CONSTANTE_VALOR_LINEA + (i+1) + ": campo Cuenta vacío", usuarioAuditoria, terminalAuditoria, ipAuditoria));
            if (isEmpty(codEnt)) observaciones.add(obs(Constante.CONSTANTE_VALOR_LINEA + (i+1) + ": campo Código entidad financiera vacío", usuarioAuditoria, terminalAuditoria, ipAuditoria));
            if (isEmpty(moneda)) observaciones.add(obs(Constante.CONSTANTE_VALOR_LINEA + (i+1) + ": campo Moneda vacío", usuarioAuditoria, terminalAuditoria, ipAuditoria));
            if (monto == null) observaciones.add(obs(Constante.CONSTANTE_VALOR_LINEA + (i+1) + ": campo Monto vacío", usuarioAuditoria, terminalAuditoria, ipAuditoria));

            if ("H".equalsIgnoreCase(tipo)) {
                hayAlMenosUnH = true;
                if (cargoActual != null) {
                    validarCargoYAbonos(cargoActual, abonosActuales, observaciones, usuarioAuditoria, terminalAuditoria, ipAuditoria);
                    abonos.addAll(abonosActuales);
                    abonosActuales = new ArrayList<>();
                }
                cargoActual = new CargoSolicitudEntity();
                cargoActual.setCuentaOrigen(cuenta);
                cargoActual.setCodigoEntidadFinanciera(codEnt);
                cargoActual.setMoneda(moneda);
                cargoActual.setMontoCargo(monto != null ? monto.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                cargoActual.setEstadoValidacion(Constante.ESTADO_VALIDACION_PENDIENTE);
                cargoActual.setEstadoEjecucion(Constante.ESTADO_EJECUCION_PENDIENTE);
                cargoActual.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());
                cargoActual.setAudiFechIns(LocalDateTime.now());
                cargoActual.setAudiUsuario(usuarioAuditoria);
                cargoActual.setAudiNomTerminal(terminalAuditoria);
                cargoActual.setAudiIp(ipAuditoria);
                cargos.add(cargoActual);
            } else if ("D".equalsIgnoreCase(tipo)) {
                if (cargoActual == null) {
                    observaciones.add(obs(Constante.CONSTANTE_VALOR_LINEA + (i+1) + ": aparece D sin cabecera H previa", usuarioAuditoria, terminalAuditoria, ipAuditoria));
                    continue;
                }
                AbonosSolicitudEntity ab = new AbonosSolicitudEntity();
                ab.setCargoSolicitud(cargoActual);
                ab.setCuentaDestino(cuenta);
                ab.setCodigoEntidadFinanciera(codEnt);
                ab.setMoneda(moneda);
                ab.setMontoDestino(monto != null ? monto.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
                ab.setEstadoEjecucion(Constante.ESTADO_EJECUCION_PENDIENTE);
                ab.setDetalleEjecucion("");
                ab.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());
                ab.setAudiFechIns(LocalDateTime.now());
                ab.setAudiUsuario(usuarioAuditoria);
                ab.setAudiNomTerminal(terminalAuditoria);
                ab.setAudiIp(ipAuditoria);
                abonosActuales.add(ab);
            } else {
                observaciones.add(obs(Constante.CONSTANTE_VALOR_LINEA + (i+1) + ": Tipo debe ser H o D", usuarioAuditoria, terminalAuditoria, ipAuditoria));
            }
        }

        if (cargoActual != null) {
            validarCargoYAbonos(cargoActual, abonosActuales, observaciones, usuarioAuditoria, terminalAuditoria, ipAuditoria);
            abonos.addAll(abonosActuales);
        }

        if (!hayAlMenosUnH) throw new IllegalArgumentException("Debe existir al menos una línea H");

        return new ParseResult(cargos, abonos, observaciones);
    }

    private void persistirResultados(SolicitudEntity solicitud,
                                     ParseResult parseResult) {
        parseResult.cargos.forEach(c -> c.setSolicitud(solicitud));
        Map<CargoSolicitudEntity, BigDecimal> sumaAbonosPorCargo = parseResult.abonos.stream()
                .collect(Collectors.groupingBy(
                        AbonosSolicitudEntity::getCargoSolicitud,
                        Collectors.reducing(BigDecimal.ZERO, a -> safe(a.getMontoDestino()), BigDecimal::add)
                ));

        parseResult.cargos.forEach(cargo ->
                cargo.setMontoTotalAbonos(sumaAbonosPorCargo.getOrDefault(cargo, BigDecimal.ZERO)
                        .setScale(2, RoundingMode.HALF_UP))
        );
        cargoRepo.saveAll(parseResult.cargos);

        abonoRepo.saveAll(parseResult.abonos);

        parseResult.observaciones.forEach(o -> o.setSolicitud(solicitud));
        obsRepo.saveAll(parseResult.observaciones);
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }

    private boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }

    private BigDecimal safe(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }
}