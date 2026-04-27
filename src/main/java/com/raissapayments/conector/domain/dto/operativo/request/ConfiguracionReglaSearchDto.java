package com.raissapayments.conector.domain.dto.operativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfiguracionReglaSearchDto extends SearchRequestDTO {
    private Long codigoRegla;
    private Long codigoCategoria;
    private String codigoModo;
    private String estadoRegistro;
}