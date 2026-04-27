package com.raissapayments.conector.service.administrativo.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaSearchDto;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaUsuarioRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.VinculoCategoriaUsuarioRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import com.raissapayments.conector.domain.dto.administrativo.response.VinculoCategoriaUsuarioResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.CategoriaEntity;
import com.raissapayments.conector.domain.entity.administrativo.CategoriaUsuarioEntity;
import com.raissapayments.conector.domain.mapper.administrativo.CategoriaMapper;
import com.raissapayments.conector.domain.repository.administrativo.CategoriaRepository;
import com.raissapayments.conector.domain.repository.administrativo.CategoriaUsuarioRepository;
import com.raissapayments.conector.service.administrativo.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl extends AbstractService implements CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final CategoriaUsuarioRepository categoriaUsuarioRepository;

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
    public Page<CategoriaResponseDto> listPage(CategoriaSearchDto searchDto) {
        Pageable pageable = buildPageable(searchDto);

        String descripcionFiltrada = (searchDto.getDescripcion() != null)
                ? searchDto.getDescripcion().trim()
                : "";

        Page<CategoriaEntity> page = categoriaRepository.findByDescripcionContainingIgnoreCaseAndEstadoRegistro(
                descripcionFiltrada,
                searchDto.getEstadoRegistro(),
                pageable
        );

        List<CategoriaResponseDto> dtos = page.getContent().stream()
                .map(categoriaMapper::entityToResponseDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> listCategorias() {
        List<CategoriaEntity> list = categoriaRepository.findByEstadoRegistroOrderById(Constante.ESTADO_ACTIVO);

        return list.stream()
                .map(categoriaMapper::entityToResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VinculoCategoriaUsuarioResponseDto listVinculoCategoriaUsuario(VinculoCategoriaUsuarioRequestDto requestDto) {
        List<CategoriaUsuarioEntity> lista =
                categoriaUsuarioRepository.findByCategoriaIdAndEstadoRegistro(
                        requestDto.getIdCategoria(),
                        Constante.ESTADO_ACTIVO
                );

        List<String> usernames = new ArrayList<>();
        for (CategoriaUsuarioEntity entity : lista) {
            usernames.add(entity.getUsername());
        }

        VinculoCategoriaUsuarioResponseDto vinculo = new VinculoCategoriaUsuarioResponseDto();
        vinculo.setIdCategoria(requestDto.getIdCategoria());
        vinculo.setUsuariosVinculados(usernames);
        return vinculo;
    }

    @Override
    @Transactional
    public Boolean vincularCategoriaUsuario(CategoriaUsuarioRequestDto requestDto) {
        try {

            Long categoriaId = requestDto.getIdCategoria();
            List<String> usernames = requestDto.getUsernames();

            // 🔹 1. Traer todos los existentes en una sola query
            List<CategoriaUsuarioEntity> existentes =
                    categoriaUsuarioRepository.findByCategoriaIdAndUsernameIn(categoriaId, usernames);

            // 🔹 2. Mapear por username para búsqueda rápida
            Map<String, CategoriaUsuarioEntity> mapExistentes = existentes.stream()
                    .collect(Collectors.toMap(CategoriaUsuarioEntity::getUsername, e -> e));

            List<CategoriaUsuarioEntity> toSave = new ArrayList<>();

            // 🔹 3. Traer la categoría una sola vez
            CategoriaEntity categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            for (String username : usernames) {
                CategoriaUsuarioEntity entity = mapExistentes.get(username);

                if (entity != null) {
                    // 🔸 Ya existe
                    if (Constante.ESTADO_INACTIVO.equals(entity.getEstadoRegistro())) {
                        // 🔹 Solo si está inactivo → activar
                        entity.setEstadoRegistro(Constante.ESTADO_ACTIVO);

                        entity.setAudiUsuMod(requestDto.getUsuarioAuditoria());
                        entity.setAudiFechaMod(requestDto.getFechaAuditoria());
                        entity.setAudiIpMod(requestDto.getIpAuditoria());
                        entity.setAudiNomTerminalMod(requestDto.getTerminalAuditoria());

                        toSave.add(entity);
                    }
                    // 🔹 Si ya está activo → NO HACER NADA
                } else {
                    // 🔸 No existe → crear
                    CategoriaUsuarioEntity nuevo = new CategoriaUsuarioEntity();
                    nuevo.setCategoria(categoria);
                    nuevo.setUsername(username);
                    nuevo.setEstadoRegistro(Constante.ESTADO_ACTIVO);

                    nuevo.setAudiUsuario(requestDto.getUsuarioAuditoria());
                    nuevo.setAudiFechIns(requestDto.getFechaAuditoria());
                    nuevo.setAudiIp(requestDto.getIpAuditoria());
                    nuevo.setAudiNomTerminal(requestDto.getTerminalAuditoria());

                    toSave.add(nuevo);
                }
            }

            // 🔹 4. Guardar en batch
            if (!toSave.isEmpty()) {
                categoriaUsuarioRepository.saveAll(toSave);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean desvincularCategoriaUsuario(CategoriaUsuarioRequestDto requestDto) {
        try {
            int updated = categoriaUsuarioRepository.desvincularUsuarios(
                    requestDto.getIdCategoria(),
                    requestDto.getUsernames(),
                    requestDto.getUsuarioAuditoria(),
                    requestDto.getFechaAuditoria(),
                    requestDto.getIpAuditoria(),
                    requestDto.getTerminalAuditoria()
            );

            return updated > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private CategoriaEntity getEntity(Long codigo) {
        return categoriaRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Categoria con código " + codigo + " no encontrada"));
    }
}