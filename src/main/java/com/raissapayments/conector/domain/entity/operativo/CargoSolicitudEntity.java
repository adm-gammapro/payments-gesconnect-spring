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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cargo_solicitud", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CargoSolicitudEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargos_generator")
    @SequenceGenerator(name = "cargos_generator", sequenceName = "public.cargo_solicitud_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_solicitud", nullable = false)
    private SolicitudEntity solicitud;

    @Column(name = "cuenta_origen", length = 100, nullable = false)
    private String cuentaOrigen;

    @Column(name = "codigo_entidad_financiera", length = 3, nullable = false)
    private String codigoEntidadFinanciera;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda;

    @Column(name = "monto_cargo", nullable = false)
    private BigDecimal montoCargo;

    @Column(name = "monto_total_abonos")
    private BigDecimal montoTotalAbonos;

    @Column(name = "estado_validacion", length = 1, nullable = false)
    private String estadoValidacion;

    @Column(name = "estado_ejecucion", length = 1, nullable = false)
    private String estadoEjecucion;

    @OneToMany(mappedBy = "cargoSolicitud", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 50)
    private List<AbonosSolicitudEntity> abonos = new ArrayList<>();
}