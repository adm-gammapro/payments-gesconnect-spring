package com.raissapayments.conector.domain.dto.operativo.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CargaSolicitudResponseDto {
    private List<Long> idsSolicitudes;
    private int totalCargos;
    private int totalAbonos;
    private int totalObservaciones;

    // Constructor actualizado
    public CargaSolicitudResponseDto(List<Long> idsSolicitudes,
                                     int totalCargos,
                                     int totalAbonos,
                                     int totalObservaciones) {
        this.idsSolicitudes = idsSolicitudes;
        this.totalCargos = totalCargos;
        this.totalAbonos = totalAbonos;
        this.totalObservaciones = totalObservaciones;
    }
}