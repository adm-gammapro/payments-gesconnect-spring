package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.config.encrypted.AESUtil;
import com.raissapayments.conector.domain.dto.operativo.request.CuentaOrdenanteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.CuentaOrdenanteSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.CuentaOrdenanteResponseDto;
import com.raissapayments.conector.domain.entity.operativo.CuentaOrdenanteEntity;
import com.raissapayments.conector.domain.mapper.operativo.CuentaOrdenanteMapper;
import com.raissapayments.conector.domain.repository.operativo.CuentaOrdenanteRepository;
import com.raissapayments.conector.service.operativo.CuentaOrdenanteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CuentaOrdenanteServiceImpl extends AbstractService implements CuentaOrdenanteService {
    private final CuentaOrdenanteRepository cuentaOrdenanteRepository;
    private final CuentaOrdenanteMapper cuentaOrdenanteMapper;

    //Utilitarios
    private final AESUtil aesUtil;

    @Override
    @Transactional
    public CuentaOrdenanteResponseDto create(CuentaOrdenanteRequestDto requestDto) throws Exception {
        log.info("Creando cuenta ordenante - Usuario: {}", requestDto.getUsuarioOrdenante());

        requestDto.setUsuarioOrdenante(aesUtil.encrypt(requestDto.getUsuarioOrdenante()));
        requestDto.setPasswordOrdenante(aesUtil.encrypt(requestDto.getPasswordOrdenante()));

        CuentaOrdenanteEntity entity = cuentaOrdenanteMapper.requestDtoToEntity(requestDto);
        entity.setEstadoRegistro(EstadoRegistroEnum.VIGENTE.getValor());

        cuentaOrdenanteRepository.save(entity);

        log.info("Cuenta ordenante creada exitosamente - ID: {}", entity.getCodigo());
        return cuentaOrdenanteMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public CuentaOrdenanteResponseDto update(CuentaOrdenanteRequestDto requestDto) throws Exception {
        log.info("Actualizando cuenta ordenante - ID: {}", requestDto.getCodigo());

        requestDto.setUsuarioOrdenante(aesUtil.encrypt(requestDto.getUsuarioOrdenante()));
        if(!_isEmpty(requestDto.getPasswordOrdenante())) {
            requestDto.setPasswordOrdenante(aesUtil.encrypt(requestDto.getPasswordOrdenante()));
        }

        CuentaOrdenanteEntity entity = getEntity(requestDto.getCodigo());
        cuentaOrdenanteMapper.update(entity, requestDto);

        cuentaOrdenanteRepository.save(entity);

        log.info("Cuenta ordenante actualizada exitosamente - ID: {}", entity.getCodigo());
        return cuentaOrdenanteMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional
    public CuentaOrdenanteResponseDto delete(CuentaOrdenanteRequestDto requestDto) {
        log.info("Eliminando cuenta ordenante - ID: {}", requestDto.getCodigo());

        CuentaOrdenanteEntity entity = getEntity(requestDto.getCodigo());
        entity.setEstadoRegistro(EstadoRegistroEnum.NO_VIGENTE.getValor());
        cuentaOrdenanteRepository.save(entity);

        log.info("Cuenta ordenante eliminada exitosamente - ID: {}", entity.getCodigo());
        return cuentaOrdenanteMapper.entityToResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaOrdenanteResponseDto get(Long codigo) throws Exception {
        CuentaOrdenanteResponseDto response = cuentaOrdenanteMapper.entityToResponseDto(getEntity(codigo));

        response.setUsuarioOrdenante(aesUtil.decrypt(response.getUsuarioOrdenante()));
        response.setPasswordOrdenante("");

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuentaOrdenanteResponseDto> listPage(CuentaOrdenanteSearchDto searchDto) {
        Pageable pageable = buildPageable(searchDto);

        String numeroCuentaFiltrado = (searchDto.getNumeroCuentaOrdenante() != null)
                ? searchDto.getNumeroCuentaOrdenante().trim()
                : "";
        String estadoRegistro = (searchDto.getEstadoRegistro() != null)
                ? searchDto.getEstadoRegistro()
                : Constante.ESTADO_ACTIVO;

        Page<CuentaOrdenanteEntity> page = cuentaOrdenanteRepository
                .findByNumeroCuentaOrdenanteContainingIgnoreCaseAndEstadoRegistro(
                        numeroCuentaFiltrado, estadoRegistro, pageable);

        List<CuentaOrdenanteResponseDto> dtos = page.getContent().stream()
                .map(cuentaOrdenanteMapper::entityToResponseDto)
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaOrdenanteResponseDto> listCuentaOrdenante() {
        List<CuentaOrdenanteEntity> list = cuentaOrdenanteRepository.findByEstadoRegistro(Constante.ESTADO_ACTIVO);

        return list.stream()
                .map(cuentaOrdenanteMapper::entityToResponseDto)
                .toList();
    }

    private CuentaOrdenanteEntity getEntity(Long codigo) {
        return cuentaOrdenanteRepository.findById(codigo)
                .orElseThrow(() -> new EntityNotFoundException("CuentaOrdenante con código " + codigo + " no encontrada"));
    }
}