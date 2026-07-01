package com.raissapayments.conector.service.commons.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.entity.commons.ConstanteSistemaEntity;
import com.raissapayments.conector.domain.repository.commons.ConstanteSistemaRepository;
import com.raissapayments.conector.service.commons.ConstanteSistemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConstanteSistemaServiceImpl implements ConstanteSistemaService {
    private final ConstanteSistemaRepository repository;

    @Override
    public String obtenerValor(String codigo) {

        return repository.findByCodigoAndEstadoRegistro(codigo, Constante.ESTADO_ACTIVO)
                .map(ConstanteSistemaEntity::getValor)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe la constante: " + codigo));
    }

    @Override
    public Integer obtenerValorInteger(String codigo) {
        return Integer.valueOf(obtenerValor(codigo));
    }

    @Override
    public Long obtenerValorLong(String codigo) {
        return Long.valueOf(obtenerValor(codigo));
    }

    @Override
    public Boolean obtenerValorBoolean(String codigo) {
        return Boolean.valueOf(obtenerValor(codigo));
    }

    @Override
    public BigDecimal obtenerValorDecimal(String codigo) {
        return new BigDecimal(obtenerValor(codigo));
    }
}
