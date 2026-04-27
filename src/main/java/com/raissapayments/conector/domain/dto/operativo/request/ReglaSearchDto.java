package com.raissapayments.conector.domain.dto.operativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReglaSearchDto extends SearchRequestDTO {
    private String descripcion;
    private String moneda;
    private String estadoRegistro;
}