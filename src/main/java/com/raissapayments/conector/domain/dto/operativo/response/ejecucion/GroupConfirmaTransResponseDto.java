package com.raissapayments.conector.domain.dto.operativo.response.ejecucion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.raissa.comun.util.Constante;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupConfirmaTransResponseDto {
    private String status;
    private String message;
    private List<ConfirmaTransGetResponseDto> listRespuestaConfirmacionTransferencia;

    public static GroupConfirmaTransResponseDto error(String message) {
        return new GroupConfirmaTransResponseDto(Constante.KEY_ERROR_CODE,
                message,
                null);
    }
}