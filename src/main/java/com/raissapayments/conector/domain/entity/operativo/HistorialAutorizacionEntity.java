package com.raissapayments.conector.domain.entity.operativo;

import com.raissa.comun.general.entity.Auditoria;
import com.raissapayments.conector.domain.entity.administrativo.CategoriaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historial_autorizacion", schema = "public")
public class HistorialAutorizacionEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_solicitud", nullable = false)
    private SolicitudEntity solicitud;

    @Column(name = "limite_inferior", nullable = false)
    private BigDecimal limiteInferior;

    @Column(name = "limite_superior", nullable = false)
    private BigDecimal limiteSuperior;

    @Column(name = "moneda", length = 3, nullable = false)
    private String moneda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_categoria", nullable = false)
    private CategoriaEntity categoria;

    @Column(name = "descripcion_categoria", length = 50, nullable = false)
    private String descripcionCategoria;

    @Column(name = "usuario_autorizador", length = 50, nullable = false)
    private String usuarioAutorizador;

    @Column(name = "fecha_autorizacion", nullable = false)
    private LocalDateTime fechaAutorizacion;
}