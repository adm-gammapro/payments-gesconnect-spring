package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.LiquidacionSolicitudRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.SolicitudSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ConstanciaPagoResponse;
import com.raissapayments.conector.domain.dto.operativo.response.DetalleLiquidacionSolicitud;
import com.raissapayments.conector.domain.dto.operativo.response.LiquidacionSolicitudResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.GestionAutorizacionSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.mapper.operativo.SolicitudMapper;
import com.raissapayments.conector.domain.repository.operativo.AbonosSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.CargoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.GestionAutorizacionSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.service.operativo.SolicitudService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl extends AbstractService implements SolicitudService {
    private final SolicitudRepository solicitudRepo;
    private final SolicitudMapper solicitudMapper;
    private final GestionAutorizacionSolicitudRepository gestionAutorizacionSolicitudRepository;
    private final AbonosSolicitudRepository abonosSolicitudRepository;
    private final CargoSolicitudRepository cargoSolicitudRepository;

    private static final DateTimeFormatter FECHA_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy")
            .withLocale(new Locale("es", "ES"));

    private static final DateTimeFormatter HORA_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a")
            .withLocale(new Locale("es", "ES"));

    private static final DateTimeFormatter FECHA_ORIGEN_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter HORA_ORIGEN_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Page<SolicitudResponseDto> getPageSolicitudes(SolicitudSearchDto filtro) {
        Pageable pageable = buildPageable(filtro);

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "fechaCarga")
        );

        LocalDateTime start = null;
        LocalDateTime end = null;
        if(!_isEmpty(filtro.getFechaInicial()) && !_isEmpty(filtro.getFechaFinal())) {
            LocalDate fechaInicio = LocalDate.parse(filtro.getFechaInicial().trim());
            LocalDate fechaFin = LocalDate.parse(filtro.getFechaFinal().trim());

            start = fechaInicio.atStartOfDay();
            end = fechaFin.plusDays(1).atStartOfDay();
        }

        Page<SolicitudEntity> page;
        if (_equiv(filtro.getProceso(), "autorizar")) {
            page = solicitudRepo.findSolicitudesPendientesParaAutorizar(filtro.getUsuarioActual(),
                    filtro.getCodigo(),
                    filtro.getUsuarios(),
                    filtro.getEstadoSolicitud(),
                    start,
                    end,
                    sortedPageable);
        } else if (_equiv(filtro.getProceso(), "cargar")) {
            page = solicitudRepo.findSolicitudesByFiltrosCargar(filtro.getUsuarios(),
                    filtro.getCodigo(),
                    filtro.getEstadoSolicitud(),
                    start,
                    end,
                    filtro.getUsuarioActual(),
                    sortedPageable);
        } else if(_equiv(filtro.getProceso(), "ejecutar")) {
            page = solicitudRepo.findSolicitudesByFiltros(filtro.getUsuarios(),
                    filtro.getCodigo(),
                    filtro.getEstadoSolicitud(),
                    start,
                    end,
                    sortedPageable);
        } else {
            page = solicitudRepo.findSolicitudesByFiltros(filtro.getUsuarios(),
                    filtro.getCodigo(),
                    filtro.getEstadoSolicitud(),
                    start,
                    end,
                    sortedPageable);
        }
        List<SolicitudResponseDto> dtos = page.getContent().stream()
                .map(solicitudMapper::entityToResponseDto)
                .toList();

        return new PageImpl<>(completarBeneficiariosValidados(dtos), pageable, page.getTotalElements());
    }

    public LiquidacionSolicitudResponseDto resumenLiquidacionSolicitud(LiquidacionSolicitudRequestDto req) {
        BigDecimal cargos = BigDecimal.ZERO;
        BigDecimal totalImpuestos = BigDecimal.ZERO;
        BigDecimal totalComisionesOrigen = BigDecimal.ZERO;
        BigDecimal totalComisionesDestino = BigDecimal.ZERO;
        String moneda = Constante.CODIGO_MONEDA_SOLES_ISO;
        List<DetalleLiquidacionSolicitud> detalles  = new ArrayList<>();

        SolicitudEntity solicitud = solicitudRepo.findByIdAndEstadoRegistro(req.getSolicitudId(), Constante.ESTADO_ACTIVO);

        if (solicitud == null) {
            throw new EntityNotFoundException("Solicitud no encontrada: " + req.getSolicitudId());
        }

        for (CargoSolicitudEntity cargo : solicitud.getCargos()) {
            cargos = cargos.add(nullSafe(cargo.getMontoCargo()));
            moneda = cargo.getMoneda();
            for (AbonosSolicitudEntity abono : cargo.getAbonos()) {
                if (isTransferenciaExitosa(abono)) {
                    DetalleLiquidacionSolicitud detalle = crearDetalleLiquidacion(abono);
                    detalles.add(detalle);

                    totalImpuestos = totalImpuestos.add(nullSafe(abono.getItf()));
                    totalComisionesOrigen = totalComisionesOrigen.add(nullSafe(abono.getComisionOrigen()));
                    totalComisionesDestino = totalComisionesDestino.add(nullSafe(abono.getComisionDestino()));
                }
            }
        }

        return LiquidacionSolicitudResponseDto.builder()
                .idSolicitud(req.getSolicitudId())
                .totalImpuestos(totalImpuestos)
                .totalComisionesOrigen(totalComisionesOrigen)
                .totalComisionesDestino(totalComisionesDestino)
                .detalle(detalles)
                .cargo(cargos)
                .totalComisiones(calcularTotalComisiones(totalComisionesOrigen, totalComisionesDestino))
                .totalCobros(calcularTotalCobros(totalImpuestos, totalComisionesOrigen, totalComisionesDestino))
                .totalLiquidacion(calcularTotalLiquidacion(totalImpuestos, totalComisionesOrigen, totalComisionesDestino, cargos))
                .moneda(moneda)
                .build();
    }

    @Transactional(readOnly = true)
    public ConstanciaPagoResponse obtenerConstanciaPago(Long abonosolicitudId) {
        AbonosSolicitudEntity entity = abonosSolicitudRepository.findById(abonosolicitudId)
                .orElseThrow(() -> new RuntimeException("Abono no encontrado"));

        CargoSolicitudEntity cargo = cargoSolicitudRepository.findById(entity.getCargoSolicitud().getId())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        ConstanciaPagoResponse response = new ConstanciaPagoResponse();

        response.setCodigoOperacion(entity.getMovimientoUid());
        response.setBancoOrigen(cargo.getCodigoEntidadFinanciera());
        response.setCuentaOrigen(cargo.getCuentaOrigen());
        response.setDestinatario(entity.getBeneficiario());
        response.setDestino(entity.getCuentaDestino());
        response.setEntidadDestino(entity.getCodigoEntidadFinanciera());
        response.setMoneda(convertirMoneda(entity.getMoneda()));
        response.setMonto(formatearMonto(entity.getMontoDestino(), entity.getMoneda()));
        response.setFecha(formatearFechaCompleta(entity));

        return response;
    }

    private List<SolicitudResponseDto> completarBeneficiariosValidados(List<SolicitudResponseDto> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        for (SolicitudResponseDto solicitud : list) {
            if (solicitud.getCargos() != null) {
                List<String> usuariosAutorizacion = new ArrayList<>();

                List<GestionAutorizacionSolicitudEntity> listGestion =
                        gestionAutorizacionSolicitudRepository.obtenerListaAutorizacionesPorPrioridad(solicitud.getId(),
                                Constante.ESTADO_GESTION_AUTORIZACION_PENDIENTE,
                                Constante.ESTADO_ACTIVO);
                if (listGestion != null && !listGestion.isEmpty()) {
                    for (GestionAutorizacionSolicitudEntity gestion : listGestion) {
                        usuariosAutorizacion.add(gestion.getUsername());
                    }
                    solicitud.setUsuariosAutorizacion(usuariosAutorizacion);
                }
            }
        }

        return list;
    }

    private boolean isTransferenciaExitosa(AbonosSolicitudEntity abono) {
        return abono != null &&
                Constante.ESTADO_ALFIN_OK.equals(abono.getEstadoEjecucionTransferencia());
    }

    private BigDecimal nullSafe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private DetalleLiquidacionSolicitud crearDetalleLiquidacion(AbonosSolicitudEntity abono) {
        return DetalleLiquidacionSolicitud.builder()
                .itf(nullSafe(abono.getItf()))
                .cciDestino(abono.getCuentaDestino())
                .cliente(abono.getNombreBeneficiarioRespuesta())
                .moneda(abono.getMoneda())
                .monto(abono.getMontoDestino())
                .comisionOrigen(nullSafe(abono.getComisionOrigen()))
                .comisionDestino(nullSafe(abono.getComisionDestino()))
                .build();
    }

    private BigDecimal calcularTotalCobros(BigDecimal impuestos, BigDecimal comisionesOrigen, BigDecimal comisionesDestino) {
        return impuestos.add(comisionesOrigen).add(comisionesDestino);
    }

    private BigDecimal calcularTotalLiquidacion(BigDecimal impuestos, BigDecimal comisionesOrigen, BigDecimal comisionesDestino, BigDecimal cargo) {
        return cargo.add(impuestos).add(comisionesOrigen).add(comisionesDestino);
    }

    private BigDecimal calcularTotalComisiones(BigDecimal comisionesOrigen, BigDecimal comisionesDestino) {
        return comisionesOrigen.add(comisionesDestino);
    }

    private String convertirMoneda(String moneda) {
        if (moneda == null) {
            return "S/";
        }

        String monedaUpper = moneda.toUpperCase();
        return switch (monedaUpper) {
            case "PEN" -> "S/";
            case "USD" -> "$";
            default -> "S/";
        };
    }

    private String formatearMonto(BigDecimal monto, String moneda) {
        if (monto == null) {
            return "S/ 0.00";
        }

        String simbolo = convertirMoneda(moneda);
        String montoFormateado = String.format("%,.2f", monto);

        return String.format("%s %s", simbolo, montoFormateado);
    }

    private String formatearFechaCompleta(AbonosSolicitudEntity entity) {
        String fechaStr = entity.getFechaTransferencia();
        String horaStr = entity.getHoraTransferencia();

        String fechaFormateada = formatearFecha(fechaStr);
        String horaFormateada = formatearHora(horaStr);

        if (fechaFormateada != null && horaFormateada != null) {
            return String.format("%s %s", fechaFormateada, horaFormateada);
        } else if (fechaFormateada != null) {
            return fechaFormateada;
        } else if (horaFormateada != null) {
            return horaFormateada;
        }

        return "Fecha no disponible";
    }

    private String formatearFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.isEmpty()) {
            return null;
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaStr, FECHA_ORIGEN_FORMATTER);
            return fecha.format(FECHA_FORMATTER);
        } catch (Exception e) {
            return fechaStr;
        }
    }

    private String formatearHora(String horaStr) {
        if (horaStr == null || horaStr.isEmpty()) {
            return null;
        }

        try {
            LocalTime hora = LocalTime.parse(horaStr, HORA_ORIGEN_FORMATTER);
            return hora.format(HORA_FORMATTER);
        } catch (Exception e) {
            return horaStr;
        }
    }
}