package com.raissapayments.conector.domain.dto.operativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CuentaOrdenanteSearchDto extends SearchRequestDTO {
    private String numeroCuentaOrdenante;
    private String estadoRegistro;
}
