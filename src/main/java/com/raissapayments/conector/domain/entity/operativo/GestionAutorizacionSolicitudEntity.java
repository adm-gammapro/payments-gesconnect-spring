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
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gestion_autorizacion_solicitud", schema = "public")
@SuperBuilder
public class GestionAutorizacionSolicitudEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gestion_autorizacion_generator")
    @SequenceGenerator(name = "gestion_autorizacion_generator", sequenceName = "public.gestion_autorizacion_solicitud_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "codigo_solicitud", nullable = false)
    private Long codigoSolicitud;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "prioridad")
    private Integer prioridad;

    @Column(name = "estado_procesamiento", length = 50)
    private String estadoProcesamiento;
}
