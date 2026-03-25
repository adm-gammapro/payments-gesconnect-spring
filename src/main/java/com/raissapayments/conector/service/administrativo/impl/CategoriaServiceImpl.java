package com.raissapayments.conector.service.administrativo.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.CategoriaEntity;
import com.raissapayments.conector.domain.mapper.administrativo.CategoriaMapper;
import com.raissapayments.conector.domain.repository.administrativo.CategoriaRepository;
import com.raissapayments.conector.service.administrativo.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional
    public CategoriaResponseDto create(CategoriaRequestDto requestDto) {
        CategoriaEntity entity = categoriaMapper.requestDtoToEntity(requestDto);
        entity.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());
        entity.setAudiFechIns(requestDto.getFechaAuditoria());
        entity.setAudiUsuario(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminal(requestDto.getTerminalAuditoria());
        entity.setAudiIp(requestDto.getIpAuditoria());
        categoriaRepository.save(entity);
        return categoriaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public CategoriaResponseDto update(CategoriaRequestDto requestDto) {
        CategoriaEntity entity = getEntity(requestDto.getCodigo());
        categoriaMapper.update(entity, requestDto);
        entity.setAudiFechaMod(requestDto.getFechaAuditoria());
        entity.setAudiUsuMod(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(requestDto.getTerminalAuditoria());
        entity.setAudiIpMod(requestDto.getIpAuditoria());
        categoriaRepository.save(entity);
        return categoriaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public CategoriaResponseDto delete(CategoriaRequestDto requestDto) {
        CategoriaEntity entity = getEntity(requestDto.getCodigo());
        entity.setEstadoRegistro(EstadoRegistroEnum.NO_VIGENTE.getValor());
        entity.setAudiFechaMod(requestDto.getFechaAuditoria());
        entity.setAudiUsuMod(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(requestDto.getTerminalAuditoria());
        entity.setAudiIpMod(requestDto.getIpAuditoria());
        categoriaRepository.save(entity);
        return categoriaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDto get(Long codigo) {
        return categoriaMapper.entityToResponseDto(getEntity(codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponseDto> listPage(String filtroDescripcion, Pageable pageable) {
        Page<CategoriaEntity> page = (filtroDescripcion == null || filtroDescripcion.isBlank())
                ? categoriaRepository.findAll(pageable)
                : categoriaRepository.findByDescripcionContainingIgnoreCase(filtroDescripcion, pageable);

        List<CategoriaResponseDto> dtos = page.getContent().stream()
                .map(categoriaMapper::entityToResponseDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    private CategoriaEntity getEntity(Long codigo) {
        return categoriaRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Categoria con código " + codigo + " no encontrada"));
    }
}