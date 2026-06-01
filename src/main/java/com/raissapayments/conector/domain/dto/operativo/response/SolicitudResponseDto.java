package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.util.List;

@Data
public class SolicitudResponseDto {
    private Long id;
    private String fechaCarga;
    private String usuarioCarga;
    private Integer cantidadOrdenes;
    private Boolean enProcesamiento;
    private String estadoSolicitud;
    private List<String> usuariosAutorizacion;
    private List<CargoSolicitudResponseDto> cargos;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}