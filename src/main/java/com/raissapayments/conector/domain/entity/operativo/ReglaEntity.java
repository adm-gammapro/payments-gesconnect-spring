package com.raissapayments.conector.domain.entity.operativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "regla", schema = "public")
public class ReglaEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long id;

    @Column(name = "descripcion", length = 50, nullable = false)
    private String descripcion;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda;

    @Column(name = "limite_inferior", nullable = false)
    private BigDecimal limiteInferior;

    @Column(name = "limite_superior", nullable = false)
    private BigDecimal limiteSuperior;
}