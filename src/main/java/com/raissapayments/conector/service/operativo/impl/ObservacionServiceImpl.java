package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ObservacionResponseDto;
import com.raissapayments.conector.domain.entity.operativo.ObservacionEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.mapper.operativo.ObservacionMapper;
import com.raissapayments.conector.domain.repository.operativo.ObservacionRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.service.operativo.ObservacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ObservacionServiceImpl implements ObservacionService {
    private static final Set<String> TIPOS_PERMITIDOS = Set.of(Constante.TIPO_OBSERVACION_OBSERVADO, Constante.TIPO_OBSERVACION_ANULADO);

    private final ObservacionRepository observacionRepository;
    private final ObservacionMapper observacionMapper;

    @Override
    @Transactional
    public ObservacionResponseDto crearObservacion(ObservacionRequestDto request) {
        if (request == null) throw new IllegalArgumentException("El request es obligatorio");

        String tipo = request.getTipoObservacion();
        if (tipo == null || !TIPOS_PERMITIDOS.contains(tipo.trim().toUpperCase())) {
            throw new IllegalArgumentException("Tipo de observación inválido. Use O o A");
        }

        Long solicitudId = request.getSolicitudId();
        if (solicitudId == null) {
            throw new IllegalArgumentException("El id de solicitud es obligatorio");
        }

        ObservacionEntity entity = observacionMapper.requestDtoToEntity(request);
        entity.setTipoObservacion(tipo.trim().toUpperCase());

        ObservacionEntity saved = observacionRepository.save(entity);
        return observacionMapper.entityToResponseDto(saved);
    }
}