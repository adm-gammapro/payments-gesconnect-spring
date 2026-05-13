package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AbonoSolicitudResponseDto {
    private Long id;
    private Long cargoSolicitudId;
    private String cuentaDestino;
    private String codigoEntidadFinanciera;
    private String moneda;
    private BigDecimal montoDestino;
    private String beneficiario;
    private String tipoDocBeneficiario;
    private String nroDocBeneficiario;
    private String mismotitular;
    private String transferenciaId;

    private BigDecimal itf;
    private BigDecimal comisionOrigen;
    private BigDecimal comisionDestino;
    private String mpe001idl;
    private String movimientoUid;
    private String codRespuestaConsulta;
    private String dscRespuestaConsulta;
    private String codRespuestaTransferencia;
    private String dscRespuestaTransferencia;
    private String estadoEjecucionConsulta;
    private String estadoEjecucionTransferencia;
    private String fechaConsulta;
    private String fechaTransferencia;
    private String horaConsulta;
    private String horaTransferencia;
    private Integer tipoDocBeneficiarioRespuesta;
    private String documentoBeneficiarioRespuesta;
    private String nombreBeneficiarioRespuesta;
    private String direccionBeneficiarioRespuesta;
    private String telefonoBeneficiarioRespuesta;
    private String movilBeneficiarioRespuesta;
    private String mismoTitularOut;

    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}