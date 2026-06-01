package com.raissapayments.conector.service.administrativo.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoSearchDto;
import com.raissapayments.conector.domain.dto.administrativo.response.TipoPagoResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.TipoPagoEntity;
import com.raissapayments.conector.domain.mapper.administrativo.TipoPagoMapper;
import com.raissapayments.conector.domain.repository.administrativo.TipoPagoRepository;
import com.raissapayments.conector.service.administrativo.TipoPagoService;
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
public class TipoPagoServiceImpl extends AbstractService implements TipoPagoService {
    private final TipoPagoRepository tipoPagoRepository;
    private final TipoPagoMapper tipoPagoMapper;

    @Override
    @Transactional
    public TipoPagoResponseDto create(TipoPagoRequestDto requestDto) {
        TipoPagoEntity entity = tipoPagoMapper.requestDtoToEntity(requestDto);
        entity.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());

        entity.setAudiFechIns(requestDto.getFechaAuditoria());
        entity.setAudiUsuario(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminal(requestDto.getTerminalAuditoria());
        entity.setAudiIp(requestDto.getIpAuditoria());

        tipoPagoRepository.save(entity);
        return tipoPagoMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public TipoPagoResponseDto update(TipoPagoRequestDto requestDto) {
        TipoPagoEntity entity = getEntity(requestDto.getCodigo());
        tipoPagoMapper.update(entity, requestDto);

        entity.setAudiFechaMod(requestDto.getFechaAuditoria());
        entity.setAudiUsuMod(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(requestDto.getTerminalAuditoria());
        entity.setAudiIpMod(requestDto.getIpAuditoria());

        tipoPagoRepository.save(entity);
        return tipoPagoMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public TipoPagoResponseDto delete(TipoPagoRequestDto requestDto) {
        TipoPagoEntity entity = getEntity(requestDto.getCodigo());
        entity.setEstadoRegistro(EstadoRegistroEnum.NO_VIGENTE.getValor());

        entity.setAudiFechaMod(requestDto.getFechaAuditoria());
        entity.setAudiUsuMod(requestDto.getUsuarioAuditoria());
        entity.setAudiNomTerminalMod(requestDto.getTerminalAuditoria());
        entity.setAudiIpMod(requestDto.getIpAuditoria());

        tipoPagoRepository.save(entity);
        return tipoPagoMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public TipoPagoResponseDto get(Long codigo) {
        return tipoPagoMapper.entityToResponseDto(getEntity(codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoPagoResponseDto> listPage(TipoPagoSearchDto searchDto) {
        Pageable pageable = buildPageable(searchDto);

        String descripcionFiltrada = (searchDto.getDescripcion() != null)
                ? searchDto.getDescripcion().trim()
                : "";

        Page<TipoPagoEntity> page = tipoPagoRepository.findByDescripcionContainingIgnoreCaseAndEstadoRegistro(
                descripcionFiltrada,
                searchDto.getEstadoRegistro(),
                pageable
        );

        List<TipoPagoResponseDto> dtos = page.getContent().stream()
                .map(tipoPagoMapper::entityToResponseDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoPagoResponseDto> listTipoPago() {
        List<TipoPagoEntity> list = tipoPagoRepository.findByEstadoRegistro(Constante.ESTADO_ACTIVO);

        return list.stream()
                .map(tipoPagoMapper::entityToResponseDto)
                .toList();
    }

    private TipoPagoEntity getEntity(Long codigo) {
        return tipoPagoRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("TipoPago con código " + codigo + " no encontrado"));
    }
}