package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.enums.commons.SortOrderEnum;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.CabeceraEjecucionResponseDto;
import com.raissapayments.conector.domain.entity.operativo.CabeceraEjecucionEntity;
import com.raissapayments.conector.domain.mapper.operativo.CabeceraEjecucionMapper;
import com.raissapayments.conector.domain.repository.operativo.CabeceraEjecucionRepository;
import com.raissapayments.conector.domain.specification.operativo.CabeceraEjecucionSpecification;
import com.raissapayments.conector.service.operativo.CabeceraEjecucionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class CabeceraEjecucionServiceImpl implements CabeceraEjecucionService {
    private final CabeceraEjecucionRepository repository;
    private final CabeceraEjecucionMapper mapper;

    @Override
    @Transactional
    public CabeceraEjecucionResponseDto registrar(CabeceraEjecucionRequestDto dto) {
        log.info("Registrando cabecera de ejecución para job: {}", dto.getCodigoJob());
        CabeceraEjecucionEntity entity = mapper.requestDtoToEntity(dto);
        entity = repository.save(entity);
        return mapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public CabeceraEjecucionResponseDto actualizar(Long id,
                                                   CabeceraEjecucionRequestDto dto) {
        log.info("Actualizando cabecera de ejecución id: {}", id);
        CabeceraEjecucionEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CabeceraEjecucion no encontrada con id: " + id));
        mapper.updateEntityFromDto(dto, entity);
        entity = repository.save(entity);
        return mapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CabeceraEjecucionResponseDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::entityToResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("CabeceraEjecucion no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CabeceraEjecucionResponseDto> buscarPaginado(CabeceraEjecucionSearchDto filtros) {

        if (filtros == null) filtros = new CabeceraEjecucionSearchDto();

        Sort sort = resolverOrden(filtros.getSortField(), filtros.getSortOrder());
        Pageable pageable = PageRequest.of(filtros.getPage(), filtros.getSize(), sort);

        Specification<CabeceraEjecucionEntity> spec =
                CabeceraEjecucionSpecification.conFiltros(filtros);

        return repository.findAll(spec, pageable)
                .map(mapper::entityToResponseDto);
    }

    // método utilitario privado (puedes moverlo a una clase base)
    private Sort resolverOrden(String sortField, SortOrderEnum sortOrder) {
        if (!StringUtils.hasText(sortField)) {
            return Sort.by(Sort.Direction.DESC, "audiFechIns");
        }
        Sort.Direction direction = SortOrderEnum.DESCENDENTE.equals(sortOrder)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        return Sort.by(direction, sortField);
    }
}
