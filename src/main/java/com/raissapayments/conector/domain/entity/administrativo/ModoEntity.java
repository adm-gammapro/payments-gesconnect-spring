package com.raissapayments.conector.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "modo", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ModoEntity extends Auditoria {
    @Id
    @Column(name = "codigo", length = 1, nullable = false)
    private String codigo;

    @Column(name = "descripcion", length = 50, nullable = false)
    private String descripcion;
}