package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class CabeceraEjecucionSearchDto extends SearchRequestDTO {
    private String estadoProcesamiento;   // PROCESANDO / FINALIZADO / FINALIZADO_ERROR
    private String fechaInicial;
    private String fechaFinal;
}