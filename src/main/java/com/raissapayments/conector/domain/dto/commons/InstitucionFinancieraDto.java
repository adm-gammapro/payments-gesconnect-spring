package com.raissapayments.conector.domain.dto.commons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitucionFinancieraDto {
    private String codigo;
    private String codigoSbs;
}
