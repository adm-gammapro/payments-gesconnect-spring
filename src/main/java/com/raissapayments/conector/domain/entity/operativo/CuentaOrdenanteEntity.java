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
@Entity
@Table(name = "cuenta_ordenante", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CuentaOrdenanteEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cuenta_ordenante_generator")
    @SequenceGenerator(name = "cuenta_ordenante_generator", sequenceName = "public.cuenta_ordenante_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "usuario_ordenante", length = 500, nullable = false)
    private String usuarioOrdenante;

    @Column(name = "password_ordenante", length = 500, nullable = false)
    private String passwordOrdenante;

    @Column(name = "numero_cuenta_ordenante", length = 50, nullable = false)
    private String numeroCuentaOrdenante;

    @Column(name = "moneda_cuenta_ordenante", length = 50, nullable = false)
    private String monedaCuentaOrdenante;

    @Column(name = "tipo_documento_ordenante", length = 10, nullable = false)
    private String tipoDocumentoOrdenante;

    @Column(name = "documento_ordenante", length = 50, nullable = false)
    private String documentoOrdenante;

    @Column(name = "nombre_ordenante", length = 500, nullable = false)
    private String nombreOrdenante;

    @Column(name = "apellido_paterno_ordenante", length = 500)
    private String apellidoPaternoOrdenante;

    @Column(name = "apellido_materno_ordenante", length = 500)
    private String apellidoMaternoOrdenante;
}
