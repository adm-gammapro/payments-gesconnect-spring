package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.SolicitudSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.AbonoSolicitudResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.CargoSolicitudResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import com.raissapayments.conector.domain.entity.operativo.GestionAutorizacionSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.RespuestaAbonoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.mapper.operativo.SolicitudMapper;
import com.raissapayments.conector.domain.repository.operativo.GestionAutorizacionSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.RespuestaAbonoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.service.operativo.SolicitudService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl extends AbstractService implements SolicitudService {
    private final SolicitudRepository solicitudRepo;
    private final SolicitudMapper solicitudMapper;
    private final RespuestaAbonoSolicitudRepository respuestaAbonoSolicitudRepository;
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

        List<Long> idsAbonos = new ArrayList<>();
        for (SolicitudResponseDto solicitud : list) {
            if (solicitud.getCargos() != null) {
                for (CargoSolicitudResponseDto cargo : solicitud.getCargos()) {
                    if (cargo.getAbonos() != null) {
                        for (AbonoSolicitudResponseDto abono : cargo.getAbonos()) {
                            idsAbonos.add(abono.getId());
                        }
                    }
                }
            }
        }

        if (idsAbonos.isEmpty()) {
            return list;
        }

        List<RespuestaAbonoSolicitudEntity> respuestas = respuestaAbonoSolicitudRepository
                .findByCodigoAbonoSolicitudInAndEstadoRegistro(idsAbonos, Constante.ESTADO_ACTIVO);

        Map<Long, RespuestaAbonoSolicitudEntity> mapaRespuestas = respuestas.stream()
                .collect(Collectors.toMap(
                        RespuestaAbonoSolicitudEntity::getCodigoAbonoSolicitud,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        for (SolicitudResponseDto solicitud : list) {
            if (solicitud.getCargos() != null) {
                List<String> usuariosAutorizacion = new ArrayList<>();
                for (CargoSolicitudResponseDto cargo : solicitud.getCargos()) {
                    if (cargo.getAbonos() != null) {
                        for (AbonoSolicitudResponseDto abono : cargo.getAbonos()) {
                            RespuestaAbonoSolicitudEntity respuesta = mapaRespuestas.get(abono.getId());

                            if (respuesta != null) {
                                abono.setNdocBeneficiarioValidado(
                                        respuesta.getDocumentoBeneficiario() != null ?
                                                respuesta.getDocumentoBeneficiario() : ""
                                );
                                abono.setNombreBeneficiarioValidado(
                                        respuesta.getNombreBeneficiario() != null ?
                                                respuesta.getNombreBeneficiario() : ""
                                );
                            } else {
                                abono.setNdocBeneficiarioValidado("");
                                abono.setNombreBeneficiarioValidado("");
                            }
                        }
                    }
                }

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
}