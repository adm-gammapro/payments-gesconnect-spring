package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.ReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ReglaSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ConfiguracionReglaResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ReglaResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.TipoPagoEntity;
import com.raissapayments.conector.domain.entity.operativo.ConfiguracionReglaEntity;
import com.raissapayments.conector.domain.entity.operativo.ReglaEntity;
import com.raissapayments.conector.domain.mapper.operativo.ReglaMapper;
import com.raissapayments.conector.domain.repository.operativo.ReglaRepository;
import com.raissapayments.conector.service.operativo.ReglaService;
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
public class ReglaServiceImpl extends AbstractService implements ReglaService {
    private final ReglaRepository reglaRepository;
    private final ReglaMapper reglaMapper;

    @Override
    @Transactional
    public ReglaResponseDto create(ReglaRequestDto requestDto) {
        ReglaEntity entity = reglaMapper.requestDtoToEntity(requestDto);
        entity.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());

        entity.setAudiFechIns(requestDto.getFechaAuditoria());
        entity.setAudiUsuario(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminal(requestDto.getTerminalAuditoria());
        entity.setAudiIp(requestDto.getIpAuditoria());

        reglaRepository.save(entity);
        return reglaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public ReglaResponseDto update(ReglaRequestDto requestDto) {
        ReglaEntity entity = getEntity(requestDto.getCodigo());
        reglaMapper.update(entity, requestDto);

        entity.setAudiFechaMod(requestDto.getFechaAuditoria());
        entity.setAudiUsuMod(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(requestDto.getTerminalAuditoria());
        entity.setAudiIpMod(requestDto.getIpAuditoria());

        reglaRepository.save(entity);
        return reglaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public ReglaResponseDto delete(ReglaRequestDto requestDto) {
        ReglaEntity entity = getEntity(requestDto.getCodigo());
        entity.setEstadoRegistro(EstadoRegistroEnum.NO_VIGENTE.getValor());

        entity.setAudiFechaMod(requestDto.getFechaAuditoria());
        entity.setAudiUsuMod(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(requestDto.getTerminalAuditoria());
        entity.setAudiIpMod(requestDto.getIpAuditoria());

        reglaRepository.save(entity);
        return reglaMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ReglaResponseDto get(Long codigo) {
        return reglaMapper.entityToResponseDto(getEntity(codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReglaResponseDto> listPage(ReglaSearchDto searchDto) {
        Pageable pageable = buildPageable(searchDto);

        String descripcionFiltrada = (searchDto.getDescripcion() != null)
                ? searchDto.getDescripcion().trim()
                : "";
        String monedaFiltrada = (searchDto.getMoneda() != null)
                ? searchDto.getMoneda().trim()
                : "";

        Page<ReglaEntity> page = reglaRepository.findByDescripcionContainingIgnoreCaseAndMonedaContainingIgnoreCaseAndEstadoRegistro(
                descripcionFiltrada,
                monedaFiltrada,
                searchDto.getEstadoRegistro(),
                pageable
        );

        List<ReglaResponseDto> dtos = page.getContent().stream()
                .map(reglaMapper::entityToResponseDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReglaResponseDto> listReglas() {
        List<ReglaEntity> list = reglaRepository.findByEstadoRegistro(Constante.ESTADO_ACTIVO);

        return list.stream()
                .map(reglaMapper::entityToResponseDto)
                .toList();
    }

    private ReglaEntity getEntity(Long codigo) {
        return reglaRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Regla con código " + codigo + " no encontrada"));
    }
}