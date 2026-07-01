package com.raissapayments.conector.service.commons;

import java.math.BigDecimal;

public interface ConstanteSistemaService {
    String obtenerValor(String codigo);

    Integer obtenerValorInteger(String codigo);

    Long obtenerValorLong(String codigo);

    Boolean obtenerValorBoolean(String codigo);

    BigDecimal obtenerValorDecimal(String codigo);
}
