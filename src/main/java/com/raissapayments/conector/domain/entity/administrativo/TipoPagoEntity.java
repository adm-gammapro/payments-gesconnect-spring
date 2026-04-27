package com.raissapayments.conector.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "tipo_pago", schema = "public")
public class TipoPagoEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_pago_generator")
    @SequenceGenerator(name = "tipo_pago_generator", sequenceName = "public.tipo_pago_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long id;

    @Column(name = "descripcion", length = 50, nullable = false)
    private String descripcion;
}