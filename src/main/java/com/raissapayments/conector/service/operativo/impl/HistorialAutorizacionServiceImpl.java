package com.raissapayments.conector.service.operativo.impl;

import com.raissapayments.conector.domain.dto.operativo.request.HistorialAutorizacionRequestDto;
import com.raissapayments.conector.domain.entity.operativo.HistorialAutorizacionEntity;
import com.raissapayments.conector.domain.mapper.operativo.HistorialAutorizacionMapper;
import com.raissapayments.conector.domain.repository.operativo.HistorialAutorizacionRepository;
import com.raissapayments.conector.service.operativo.HistorialAutorizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HistorialAutorizacionServiceImpl implements HistorialAutorizacionService {
    private final HistorialAutorizacionRepository repository;
    private final HistorialAutorizacionMapper mapper;

    @Override
    @Transactional
    public HistorialAutorizacionEntity crear(HistorialAutorizacionRequestDto request) {
        if (request == null) throw new IllegalArgumentException("Request obligatorio");

        HistorialAutorizacionEntity entity = mapper.requestDtoToEntity(request);
        return repository.save(entity);
    }
}