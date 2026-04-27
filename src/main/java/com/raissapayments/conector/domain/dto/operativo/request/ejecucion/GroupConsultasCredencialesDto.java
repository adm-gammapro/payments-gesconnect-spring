package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import com.raissapayments.conector.domain.dto.operativo.request.LoginAlfinRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class GroupConsultasCredencialesDto {
    private List<ConsultaTransRequestDto> listConsulta;
    private LoginAlfinRequestDto loginAlfin;
    private String numeroCuentaCargo;
    private BigDecimal montoCargo;
}
