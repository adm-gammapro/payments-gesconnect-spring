package com.raissapayments.conector.domain.entity.operativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "respuesta_abonos_solicitud", schema = "public")
@Getter
@Setter
public class RespuestaAbonoSolicitudEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "respuesta_abonos_solicitud_generator")
    @SequenceGenerator(name = "respuesta_abonos_solicitud_generator", sequenceName = "public.respuesta_abonos_solicitud_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "codigo_solicitud", nullable = false)
    private Long codigoSolicitud;

    @Column(name = "codigo_cargo_solicitud", nullable = false)
    private Long codigoCargoSolicitud;

    @Column(name = "codigo_abono_solicitud", nullable = false)
    private Long codigoAbonoSolicitud;

    @Column(name = "tipo_doc_beneficiario")
    private Integer tipoDocBeneficiario;

    @Column(name = "documento_beneficiario", length = 20)
    private String documentoBeneficiario;

    @Column(name = "nombre_beneficiario", length = 500)
    private String nombreBeneficiario;

    @Column(name = "direccion_beneficiario", length = 500)
    private String direccionBeneficiario;

    @Column(name = "telefono_beneficiario")
    private String telefonoBeneficiario;

    @Column(name = "movil_beneficiario", length = 20)
    private String movilBeneficiario;

    @Column(name = "mismo_titular_out", length = 1)
    private String mismoTitularOut;

    @Column(name = "transferencia_id", length = 100)
    private String transferenciaId;

    @Column(name = "itf", precision = 10, scale = 2)
    private BigDecimal itf;

    @Column(name = "comision_origen", precision = 10, scale = 2)
    private BigDecimal comisionOrigen;

    @Column(name = "comision_destino", precision = 10, scale = 2)
    private BigDecimal comisionDestino;

    @Column(name = "mpe001idl", length = 100)
    private String mpe001idl;

    @Column(name = "movimiento_uid", length = 100)
    private String movimientoUid;

    @Column(name = "cod_respuesta_consulta", length = 20)
    private String codRespuestaConsulta;

    @Column(name = "dsc_respuesta_consulta", length = 500)
    private String dscRespuestaConsulta;

    @Column(name = "cod_respuesta_transferencia", length = 20)
    private String codRespuestaTransferencia;

    @Column(name = "dsc_respuesta_transferencia", length = 500)
    private String dscRespuestaTransferencia;

    @Column(name = "error_consulta", length = 2000)
    private String errorConsulta;

    @Column(name = "error_transferencia", length = 2000)
    private String errorTransferencia;

    @Column(name = "estado_ejecucion_consulta", length = 20)
    private String estadoEjecucionConsulta;

    @Column(name = "estado_ejecucion_transferencia", length = 20)
    private String estadoEjecucionTransferencia;

    @Column(name = "fecha_consulta", length = 20)
    private String fechaConsulta;

    @Column(name = "fecha_transferencia", length = 20)
    private String fechaTransferencia;

    @Column(name = "hora_consulta", length = 20)
    private String horaConsulta;

    @Column(name = "hora_transferencia", length = 20)
    private String horaTransferencia;
}