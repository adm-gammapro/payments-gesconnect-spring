package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupConfirmaTransBffRequestDto {
    private List<ConfirmaTransRequestDto> listConfirmacionTransferencia;
}
