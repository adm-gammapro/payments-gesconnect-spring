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
public class GroupConsultaTransResponseDto {
    private String status;
    private String message;
    private List<ConsultaTransGetResponseDto> listRespuestaConsultaTransferencia;

    public static GroupConsultaTransResponseDto error(String message) {
        return new GroupConsultaTransResponseDto(Constante.KEY_ERROR_CODE,
                message,
                null);
    }
}