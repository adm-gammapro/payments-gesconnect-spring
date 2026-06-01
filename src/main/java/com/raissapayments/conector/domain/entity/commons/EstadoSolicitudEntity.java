package com.raissapayments.conector.domain.entity.commons;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "estado_solicitud", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EstadoSolicitudEntity extends Auditoria {
    @Id
    @Column(name = "codigo", length = 30, nullable = false)
    private String codigo;

    @Column(name = "descripcion", length = 50, nullable = false)
    private String descripcion;
}
