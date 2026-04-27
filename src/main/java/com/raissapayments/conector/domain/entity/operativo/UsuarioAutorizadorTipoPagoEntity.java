package com.raissapayments.conector.domain.entity.operativo;

import com.raissa.comun.general.entity.Auditoria;
import com.raissapayments.conector.domain.entity.administrativo.TipoPagoEntity;
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
@Table(name = "usuario_autorizador_tipo_pago", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UsuarioAutorizadorTipoPagoEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_autorizador_generator")
    @SequenceGenerator(name = "usuario_autorizador_generator", sequenceName = "public.usuario_autorizador_tipo_pago_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long id;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_tipo_pago", nullable = false)
    private TipoPagoEntity tipoPago;
}