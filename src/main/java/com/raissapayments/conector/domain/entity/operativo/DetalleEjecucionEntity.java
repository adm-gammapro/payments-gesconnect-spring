package com.raissapayments.conector.domain.entity.operativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "detalle_ejecucion", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DetalleEjecucionEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_ejecucion_generator")
    @SequenceGenerator(name = "detalle_ejecucion_generator",
            sequenceName = "public.detalle_ejecucion_codigo_seq",
            allocationSize = 1)
    @Column(name = "codigo")
    private Long id;

    @Column(name = "codigo_cabecera_ejecucion", nullable = false)
    private Long codigoCabeceraEjecucion;

    @Column(name = "codigo_cuenta")
    private Long codigoCuenta;

    @Column(name = "numero_cuenta", length = 30, nullable = false)
    private String numeroCuenta;

    @Column(name = "codigo_entidad_financiera", length = 3)
    private String codigoEntidadFinanciera;

    @Column(name = "nombre_entidad_financiera", length = 100, nullable = false)
    private String nombreEntidadFinanciera;

    @Column(name = "moneda_cuenta", length = 3, nullable = false)
    private String monedaCuenta;

    @Column(name = "descripcion_moneda", length = 30)
    private String descripcionMoneda;

    @Column(name = "estado_procesamiento", length = 20, nullable = false)
    private String estadoProcesamiento;

    @Column(name = "detalle_ejecucion", length = 2000)
    private String detalleEjecucion;
}
