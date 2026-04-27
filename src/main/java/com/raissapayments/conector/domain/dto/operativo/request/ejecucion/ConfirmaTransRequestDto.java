package com.raissapayments.conector.domain.dto.operativo.request.ejecucion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmaTransRequestDto {
    private String status;
    private String message;

    private Long idSolicitud;
    private Long idCargoSolicitud;
    private Long idAbonoSolicitud;
    private String clienteBaaS;
    private String cuentaBaaS;
    private String moneda;
    private BigDecimal importe;
    private String transferenciaId;
    private String mpe001idl;
}
