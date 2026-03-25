package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioAutorizadorTipoPagoDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioAutorizadorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioAutorizadorTipoPagoResponseDto;
import com.raissapayments.conector.domain.entity.operativo.UsuarioAutorizadorTipoPagoEntity;
import com.raissapayments.conector.domain.mapper.operativo.UsuarioAutorizadorTipoPagoMapper;
import com.raissapayments.conector.domain.repository.operativo.UsuarioAutorizadorTipoPagoRepository;
import com.raissapayments.conector.service.operativo.UsuarioAutorizadorTipoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioAutorizadorTipoPagoServiceImpl implements UsuarioAutorizadorTipoPagoService {
    private final UsuarioAutorizadorTipoPagoRepository repository;
    private final UsuarioAutorizadorTipoPagoMapper mapper;

    @Override
    @Transactional
    public UsuarioAutorizadorTipoPagoResponseDto crear(UsuarioAutorizadorTipoPagoRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request obligatorio");

        UsuarioAutorizadorTipoPagoEntity entity = mapper.requestDtoToEntity(request);
        UsuarioAutorizadorTipoPagoEntity saved = repository.save(entity);
        return mapper.entityToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioAutorizadorTipoPagoResponseDto> listarActivos() {
        return repository.findAll().stream()
                .filter(e -> Constante.ESTADO_ACTIVO.equals(e.getEstadoRegistro()))
                .map(mapper::entityToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void eliminarLogico(UsuarioAutorizadorTipoPagoDeleteRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request obligatorio");
        if (request.getId() == null) throw new IllegalArgumentException("Id obligatorio");

        UsuarioAutorizadorTipoPagoEntity entity = repository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Registro no encontrado: " + request.getId()));

        entity.setEstadoRegistro(Constante.ESTADO_INACTIVO);
        entity.setAudiFechaMod(request.getFechaAuditoria());
        entity.setAudiUsuMod(request.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(request.getTerminalAuditoria());
        entity.setAudiIpMod(request.getIpAuditoria());

        repository.save(entity);
    }
}