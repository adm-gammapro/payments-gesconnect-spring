package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import com.raissapayments.conector.domain.dto.operativo.request.LoginAlfinRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupConfirmaCredencialesDto {
    private List<ConfirmaTransRequestDto> listConfirmaciones;
    private LoginAlfinRequestDto loginAlfin;
}
