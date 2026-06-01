package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class DetalleEjecucionSearchDto extends SearchRequestDTO {
    private Long codigoCabeceraEjecucion;
}