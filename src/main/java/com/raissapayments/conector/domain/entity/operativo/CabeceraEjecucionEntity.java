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

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cabecera_ejecucion", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CabeceraEjecucionEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cabecera_ejecucion_generator")
    @SequenceGenerator(name = "cabecera_ejecucion_generator",
            sequenceName = "public.cabecera_ejecucion_codigo_seq",
            allocationSize = 1)
    @Column(name = "codigo")
    private Long id;

    @Column(name = "codigo_job")
    private Long codigoJob;

    @Column(name = "codigo_sistema", length = 3)
    private String codigoSistema;

    @Column(name = "codigo_cliente")
    private Long codigoCliente;

    @Column(name = "fecha_inicio_proceso")
    private LocalDateTime fechaInicioProceso;

    @Column(name = "fecha_fin_proceso")
    private LocalDateTime fechaFinProceso;

    @Column(name = "estado_procesamiento", length = 20, nullable = false)
    private String estadoProcesamiento;

    @Column(name = "proceso", length = 20)
    private String proceso;

    @Column(name = "detalle_ejecucion", length = 2000)
    private String detalleEjecucion;

    @Column(name = "registros_totales", nullable = false)
    private Integer registrosTotales;

    @Column(name = "registro_procesados", nullable = false)
    private Integer registrosProcesados;

    @Column(name = "registros_erroneos", nullable = false)
    private Integer registrosErroneos;

    @Column(name = "registro_pendientes", nullable = false)
    private Integer registrosPendientes;
}
