package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.service.AbstractService;
import com.raissapayments.conector.domain.dto.operativo.request.ConfiguracionReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ConfiguracionReglaSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ConfiguracionReglaResponseDto;
import com.raissapayments.conector.domain.entity.operativo.ConfiguracionReglaEntity;
import com.raissapayments.conector.domain.mapper.operativo.ConfiguracionReglaMapper;
import com.raissapayments.conector.domain.repository.operativo.ConfiguracionReglaRepository;
import com.raissapayments.conector.service.operativo.ConfiguracionReglaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfiguracionReglaServiceImpl extends AbstractService implements ConfiguracionReglaService {
    private final ConfiguracionReglaRepository configuracionReglaRepository;
    private final ConfiguracionReglaMapper configuracionReglaMapper;

    @Override
    @Transactional
    public ConfiguracionReglaResponseDto create(ConfiguracionReglaRequestDto dto) {
        ConfiguracionReglaEntity entity = configuracionReglaMapper.requestDtoToEntity(dto);

        configuracionReglaRepository.save(entity);
        return configuracionReglaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public ConfiguracionReglaResponseDto update(ConfiguracionReglaRequestDto dto) {
        ConfiguracionReglaEntity entity = getEntity(dto.getCodigo());
        configuracionReglaMapper.update(entity, dto);

        // Auditoría mod
        entity.setAudiFechaMod(dto.getFechaAuditoria());
        entity.setAudiUsuMod(dto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(dto.getTerminalAuditoria());
        entity.setAudiIpMod(dto.getIpAuditoria());

        configuracionReglaRepository.save(entity);
        return configuracionReglaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public ConfiguracionReglaResponseDto delete(ConfiguracionReglaRequestDto dto) {
        ConfiguracionReglaEntity entity = getEntity(dto.getCodigo());
        entity.setEstadoRegistro(EstadoRegistroEnum.NO_VIGENTE.getValor());

        entity.setAudiFechaMod(dto.getFechaAuditoria());
        entity.setAudiUsuMod(dto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(dto.getTerminalAuditoria());
        entity.setAudiIpMod(dto.getIpAuditoria());

        configuracionReglaRepository.save(entity);
        return configuracionReglaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ConfiguracionReglaResponseDto get(Long codigo) {
        return configuracionReglaMapper.entityToResponseDto(getEntity(codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConfiguracionReglaResponseDto> search(ConfiguracionReglaSearchDto searchDto) {
        Pageable pageable = buildPageable(searchDto);

        Page<ConfiguracionReglaEntity> page = configuracionReglaRepository.search(
                searchDto.getCodigoRegla(),
                searchDto.getCodigoCategoria(),
                searchDto.getCodigoModo(),
                searchDto.getEstadoRegistro(),
                pageable
        );

        var dtos = page.getContent().stream()
                .map(configuracionReglaMapper::entityToResponseDto)
                .toList();
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    private ConfiguracionReglaEntity getEntity(Long id) {
        return configuracionReglaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ConfiguracionRegla " + id + " no encontrada"));
    }
}