package com.raissapayments.conector.domain.dto.administrativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TipoPagoSearchDto extends SearchRequestDTO {
    private String descripcion;
    private String estadoRegistro;
}