package com.raissapayments.conector.domain.dto.operativo.request;

import com.raissa.comun.general.dto.SearchRequestDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class SolicitudSearchDto extends SearchRequestDTO {
    private String usuario;
    private String fechaInicial;
    private String fechaFinal;
    private String codigo;
    private List<String> estadoSolicitud;
}