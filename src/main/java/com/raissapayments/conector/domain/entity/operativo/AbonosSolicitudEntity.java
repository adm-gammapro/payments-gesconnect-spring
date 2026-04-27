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

    @Column(name = "beneficiario", length = 500)
    private String beneficiario;

    @Column(name = "mismo_titular", length = 1)
    private String mismotitular;

    @Column(name = "estado_ejecucion", length = 1, nullable = false)
    private String estadoEjecucion;

    @Column(name = "detalle_ejecucion", length = 1000, nullable = false)
    private String detalleEjecucion;
}