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

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "observacion", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ObservacionEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "observacion_generator")
    @SequenceGenerator(name = "observacion_generator", sequenceName = "public.observacion_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_solicitud", nullable = false)
    private SolicitudEntity solicitud;

    @Column(name = "descripcion", length = 2000, nullable = false)
    private String descripcion;

    @Column(name = "tipo_observacion", length = 1, nullable = false)
    private String tipoObservacion;

    @Column(name = "evento", length = 1, nullable = false)
    private String evento;

    @Column(name = "usuario", length = 1, nullable = false)
    private String usuario;
}