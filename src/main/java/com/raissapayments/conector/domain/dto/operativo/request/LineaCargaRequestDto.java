package com.raissapayments.conector.domain.dto.operativo.request;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Valid
public class LineaCargaRequestDto {
    String tipo;
    String cuenta;
    String codigoEntidadFinanciera;
    String moneda;
    BigDecimal monto;
    String beneficiario;
    String mismoTitular;
}