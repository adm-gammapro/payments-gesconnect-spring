package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioCategoriaDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioCategoriaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioCategoriaResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.CategoriaEntity;
import com.raissapayments.conector.domain.entity.operativo.UsuarioCategoriaEntity;
import com.raissapayments.conector.domain.mapper.operativo.UsuarioCategoriaMapper;
import com.raissapayments.conector.domain.repository.administrativo.CategoriaRepository;
import com.raissapayments.conector.domain.repository.operativo.UsuarioCategoriaRepository;
import com.raissapayments.conector.service.operativo.UsuarioCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioCategoriaServiceImpl implements UsuarioCategoriaService {
    private final UsuarioCategoriaRepository usuarioCategoriaRepository;
    private final CategoriaRepository categoriaRepository; // asumiendo que existe
    private final UsuarioCategoriaMapper mapper;

    @Override
    @Transactional
    public UsuarioCategoriaResponseDto crear(UsuarioCategoriaRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request obligatorio");

        UsuarioCategoriaEntity entity = mapper.requestDtoToEntity(request);
        UsuarioCategoriaEntity saved = usuarioCategoriaRepository.save(entity);
        return mapper.entityToResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioCategoriaResponseDto> listarActivos() {
        return usuarioCategoriaRepository.findAll().stream()
                .filter(e -> Constante.ESTADO_ACTIVO.equals(e.getEstadoRegistro()))
                .map(mapper::entityToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void eliminarLogico(UsuarioCategoriaDeleteRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request obligatorio");
        if (request.getId() == null) throw new IllegalArgumentException("Id obligatorio");

        UsuarioCategoriaEntity entity = usuarioCategoriaRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario-categoría no encontrado: " + request.getId()));

        entity.setEstadoRegistro(Constante.ESTADO_INACTIVO);
        entity.setAudiFechaMod(LocalDateTime.now());
        entity.setAudiUsuMod(request.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(request.getTerminalAuditoria());
        entity.setAudiIpMod(request.getIpAuditoria());

        usuarioCategoriaRepository.save(entity);
    }
}