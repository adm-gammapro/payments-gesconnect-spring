package com.raissapayments.conector.domain.entity.administrativo;

import com.raissa.comun.general.entity.Auditoria;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
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
@Table(name = "usuario_categoria", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CategoriaUsuarioEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_usuario_generator")
    @SequenceGenerator(name = "categoria_usuario_generator", sequenceName = "public.usuario_categoria_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_categoria", nullable = false)
    private CategoriaEntity categoria;

    @Column(name = "username", length = 50, nullable = false)
    private String username;
}