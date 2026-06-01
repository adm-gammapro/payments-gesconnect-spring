package com.raissapayments.conector.service.commons.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.commons.EstadoSolicitudDto;
import com.raissapayments.conector.domain.entity.commons.EstadoSolicitudEntity;
import com.raissapayments.conector.domain.mapper.commons.EstadoSolicitudMapper;
import com.raissapayments.conector.domain.repository.commons.EstadoSolicitudRepository;
import com.raissapayments.conector.service.commons.GeneralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeneralServiceImpl implements GeneralService {
    private final EstadoSolicitudRepository estadoSolicitudRepository;
    private final EstadoSolicitudMapper estadoSolicitudMapper;

    public List<EstadoSolicitudDto> listarEstadosActivos() {
        try {
            log.info("Consultando estados con estadoRegistro = 'S'");
            List<EstadoSolicitudEntity> estados = estadoSolicitudRepository.findByEstadoRegistroOrderByDescripcionAsc(Constante.ESTADO_ACTIVO);
            log.info("Se encontraron {} estados activos", estados.size());
            return estados.stream()
                    .map(estadoSolicitudMapper::entityToResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error al consultar estados activos: {}", e.getMessage(), e);
            throw new RuntimeException("Error al obtener la lista de estados activos", e);
        }
    }
}
