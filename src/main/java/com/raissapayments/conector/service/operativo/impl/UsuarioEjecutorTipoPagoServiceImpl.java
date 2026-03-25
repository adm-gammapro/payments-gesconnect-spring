package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioEjecutorTipoPagoDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioEjecutorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioEjecutorTipoPagoResponseDto;
import com.raissapayments.conector.domain.entity.operativo.UsuarioEjecutorTipoPagoEntity;
import com.raissapayments.conector.domain.mapper.operativo.UsuarioEjecutorTipoPagoMapper;
import com.raissapayments.conector.domain.repository.operativo.UsuarioEjecutorTipoPagoRepository;
import com.raissapayments.conector.service.operativo.UsuarioEjecutorTipoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioEjecutorTipoPagoServiceImpl implements UsuarioEjecutorTipoPagoService {
    private final UsuarioEjecutorTipoPagoRepository repository;
    private final UsuarioEjecutorTipoPagoMapper mapper;

    @Override
    @Transactional
    public UsuarioEjecutorTipoPagoResponseDto crear(UsuarioEjecutorTipoPagoRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request obligatorio");

        UsuarioEjecutorTipoPagoEntity entity = mapper.requestDtoToEntity(request);
        UsuarioEjecutorTipoPagoEntity saved = repository.save(entity);
        return mapper.entityToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioEjecutorTipoPagoResponseDto> listarActivos() {
        return repository.findAll().stream()
                .filter(e -> Constante.ESTADO_ACTIVO.equals(e.getEstadoRegistro()))
                .map(mapper::entityToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void eliminarLogico(UsuarioEjecutorTipoPagoDeleteRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request obligatorio");
        if (request.getId() == null) throw new IllegalArgumentException("Id obligatorio");

        UsuarioEjecutorTipoPagoEntity entity = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado: " + request.getId()));

        entity.setEstadoRegistro(Constante.ESTADO_INACTIVO);
        entity.setAudiFechaMod(request.getFechaAuditoria());
        entity.setAudiUsuMod(request.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(request.getTerminalAuditoria());
        entity.setAudiIpMod(request.getIpAuditoria());

        repository.save(entity);
    }
}