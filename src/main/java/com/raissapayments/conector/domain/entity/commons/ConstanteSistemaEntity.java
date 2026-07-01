package com.raissapayments.conector.domain.entity.commons;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "constante_sistema", schema = "public")
public class ConstanteSistemaEntity extends Auditoria {
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "descripcion", length = 250)
    private String descripcion;

    @Column(name = "valor", length = 2000)
    private String valor;
}