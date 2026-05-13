package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.LiquidacionSolicitudRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.SolicitudSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.DetalleLiquidacionSolicitud;
import com.raissapayments.conector.domain.dto.operativo.response.LiquidacionSolicitudResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.GestionAutorizacionSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.mapper.operativo.SolicitudMapper;
import com.raissapayments.conector.domain.repository.operativo.GestionAutorizacionSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.service.operativo.SolicitudService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl extends AbstractService implements SolicitudService {
    private final SolicitudRepository solicitudRepo;
    private final SolicitudMapper solicitudMapper;
    private final GestionAutorizacionSolicitudRepository gestionAutorizacionSolicitudRepository;

    public Page<SolicitudResponseDto> getPageSolicitudes(SolicitudSearchDto filtro) {
        Pageable pageable = buildPageable(filtro);

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "fechaCarga")
        );

        Page<SolicitudEntity> page = solicitudRepo.findAll(buildSpec(filtro), sortedPageable);
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

    private Specification<SolicitudEntity> buildSpec(SolicitudSearchDto f) {
        return (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();

            if (f.getUsuario() != null && !f.getUsuario().isBlank()) {
                p.add(cb.equal(cb.lower(root.get("usuarioCarga")), f.getUsuario().toLowerCase()));
            }
            if (f.getCodigo() != null && !f.getCodigo().isBlank()) {
                p.add(cb.equal(root.get("id"), Long.valueOf(f.getCodigo())));
            }
            if (f.getEstadoSolicitud() != null && !f.getEstadoSolicitud().isEmpty()) {
                Expression<String> estadoCodigo = root.get("estadoSolicitud").get("codigo");
                p.add(estadoCodigo.in(f.getEstadoSolicitud()));
            }
            if (f.getFechaInicial() != null && !f.getFechaInicial().isBlank() &&
                f.getFechaFinal() != null && !f.getFechaFinal().isBlank()) {
                LocalDate fechaInicio = LocalDate.parse(f.getFechaInicial().trim());
                LocalDate fechaFin = LocalDate.parse(f.getFechaFinal().trim());

                LocalDateTime start = fechaInicio.atStartOfDay();
                LocalDateTime end = fechaFin.plusDays(1).atStartOfDay();

                p.add(cb.between(root.get("fechaCarga"), start, end));
            }
            return cb.and(p.toArray(new Predicate[0]));
        };
    }

    private List<SolicitudResponseDto> completarBeneficiariosValidados(List<SolicitudResponseDto> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        for (SolicitudResponseDto solicitud : list) {
            if (solicitud.getCargos() != null) {
                List<String> usuariosAutorizacion = new ArrayList<>();

                List<GestionAutorizacionSolicitudEntity> listGestion =
                        gestionAutorizacionSolicitudRepository.findByCodigoSolicitudAndEstadoProcesamientoAndEstadoRegistro(solicitud.getId(),
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
}