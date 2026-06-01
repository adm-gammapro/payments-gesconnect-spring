package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.enums.commons.SortOrderEnum;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.DetalleEjecucionResponseDto;
import com.raissapayments.conector.domain.entity.operativo.DetalleEjecucionEntity;
import com.raissapayments.conector.domain.mapper.operativo.DetalleEjecucionMapper;
import com.raissapayments.conector.domain.repository.operativo.DetalleEjecucionRepository;
import com.raissapayments.conector.domain.specification.operativo.DetalleEjecucionSpecification;
import com.raissapayments.conector.service.operativo.DetalleEjecucionService;
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
public class DetalleEjecucionServiceImpl implements DetalleEjecucionService {
    private final DetalleEjecucionRepository repository;
    private final DetalleEjecucionMapper mapper;

    @Override
    @Transactional
    public DetalleEjecucionResponseDto registrar(DetalleEjecucionRequestDto dto) {
        log.info("Registrando detalle para cabecera: {}", dto.getCodigoCabeceraEjecucion());
        DetalleEjecucionEntity entity = mapper.requestDtoToEntity(dto);
        entity = repository.save(entity);
        return mapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public DetalleEjecucionResponseDto actualizar(Long id,
                                                  DetalleEjecucionRequestDto dto) {
        log.info("Actualizando detalle de ejecución id: {}", id);
        DetalleEjecucionEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DetalleEjecucion no encontrado con id: " + id));
        mapper.updateEntityFromDto(dto, entity);
        entity = repository.save(entity);
        return mapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public DetalleEjecucionResponseDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::entityToResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "DetalleEjecucion no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetalleEjecucionResponseDto> buscarPaginado(
            DetalleEjecucionSearchDto filtros) {

        if (filtros == null) filtros = new DetalleEjecucionSearchDto();

        Sort sort = resolverOrden(filtros.getSortField(), filtros.getSortOrder());
        Pageable pageable = PageRequest.of(filtros.getPage(), filtros.getSize(), sort);

        Specification<DetalleEjecucionEntity> spec =
                DetalleEjecucionSpecification.conFiltros(filtros);

        return repository.findAll(spec, pageable)
                .map(mapper::entityToResponseDto);
    }

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
