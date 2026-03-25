package com.raissapayments.conector.service.operativo.impl;

import com.raissapayments.conector.domain.dto.operativo.request.SolicitudSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import com.raissapayments.conector.domain.entity.commons.EstadoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.mapper.operativo.SolicitudMapper;
import com.raissapayments.conector.domain.repository.commons.EstadoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.exception.operativo.RecursoNoEncontradoException;
import com.raissapayments.conector.service.operativo.SolicitudService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService {
    private final SolicitudRepository solicitudRepo;
    private final EstadoSolicitudRepository estadoRepo;
    private final SolicitudMapper solicitudMapper;

    public Page<SolicitudResponseDto> getPageSolicitudes(SolicitudSearchDto filtro,
                                                         Pageable pageable) {
        Page<SolicitudEntity> page = solicitudRepo.findAll(buildSpec(filtro), pageable);
        List<SolicitudResponseDto> dtos = page.getContent().stream()
                .map(solicitudMapper::entityToResponseDto)
                .toList();
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public SolicitudEntity actualizarEstadoSolicitud(Long solicitudId, String nuevoEstadoId) {
        SolicitudEntity solicitud = solicitudRepo.findById(solicitudId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + solicitudId));

        EstadoSolicitudEntity nuevoEstado = estadoRepo.findById(nuevoEstadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("EstadoSolicitud no encontrado: " + nuevoEstadoId));

        solicitud.setEstadoSolicitud(nuevoEstado);
        return solicitud;
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
            if (f.getEstadoSolicitud() != null && !f.getEstadoSolicitud().isBlank()) {
                p.add(cb.equal(root.get("estadoSolicitud").get("codigo"), f.getEstadoSolicitud()));
            }
            if (f.getFecha() != null && !f.getFecha().isBlank()) {
                LocalDate d = LocalDate.parse(f.getFecha());
                LocalDateTime start = d.atStartOfDay();
                LocalDateTime end = d.plusDays(1).atStartOfDay();
                p.add(cb.between(root.get("fechaCarga"), start, end));
            }
            return cb.and(p.toArray(new Predicate[0]));
        };
    }
}