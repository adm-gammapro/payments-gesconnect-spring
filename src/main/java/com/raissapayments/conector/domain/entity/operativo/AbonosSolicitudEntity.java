package com.raissapayments.conector.domain.entity.operativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "abonos_solicitud", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AbonosSolicitudEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "abonos_generator")
    @SequenceGenerator(name = "abonos_generator", sequenceName = "public.abonos_solicitud_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_cargo_solicitud", nullable = false)
    private CargoSolicitudEntity cargoSolicitud;

    @Column(name = "cuenta_destino", length = 100, nullable = false)
    private String cuentaDestino;

    @Column(name = "codigo_entidad_financiera", length = 3, nullable = false)
    private String codigoEntidadFinanciera;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda;

    @Column(name = "monto_destino", nullable = false)
    private BigDecimal montoDestino;

    @Column(name = "tipo_doc_beneficiario", length = 10)
    private String tipoDocBeneficiario;

    @Column(name = "nro_doc_beneficiario", length = 50)
    private String nroDocBeneficiario;

    @Column(name = "beneficiario", length = 500)
    private String beneficiario;

    @Column(name = "mismo_titular", length = 1)
    private String mismotitular;

    // Nuevos campos desde respuesta_abonos_solicitud
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

    @Column(name = "dsc_respuesta_consulta", length = 2000)
    private String dscRespuestaConsulta;

    @Column(name = "cod_respuesta_transferencia", length = 20)
    private String codRespuestaTransferencia;

    @Column(name = "dsc_respuesta_transferencia", length = 2000)
    private String dscRespuestaTransferencia;

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

    @Column(name = "tipo_doc_beneficiario_respuesta")
    private Integer tipoDocBeneficiarioRespuesta;

    @Column(name = "documento_beneficiario_respuesta", length = 20)
    private String documentoBeneficiarioRespuesta;

    @Column(name = "nombre_beneficiario_respuesta", length = 500)
    private String nombreBeneficiarioRespuesta;

    @Column(name = "direccion_beneficiario_respuesta", length = 500)
    private String direccionBeneficiarioRespuesta;

    @Column(name = "telefono_beneficiario_respuesta", length = 20)
    private String telefonoBeneficiarioRespuesta;

    @Column(name = "movil_beneficiario_respuesta", length = 20)
    private String movilBeneficiarioRespuesta;

    @Column(name = "mismo_titular_out", length = 1)
    private String mismoTitularOut;
}