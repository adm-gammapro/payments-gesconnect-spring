package com.raissapayments.conector.domain.entity.operativo;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "response_transferencia", schema = "public")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResponseTransferenciaEntity extends Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "response_transferencia")
    @SequenceGenerator(name = "response_transferencia", sequenceName = "public.response_transferencia_codigo_seq", allocationSize = 1)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "id_solicitud")
    private Long idSolicitud;

    @Column(name = "id_cargo_solicitud")
    private Long idCargoSolicitud;

    @Column(name = "id_abono_solicitud")
    private Long idAbonoSolicitud;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "message", length = 2000)
    private String message;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode responseBffJson;

    @Column(name = "codigo_cabecera_ejecucion")
    private Long codigoCabeceraEjecucion;

    @Column(name = "codigo_detalle_ejecucion")
    private Long codigoDetalleEjecucion;

    @Column(name = "proceso", length = 20)
    private String proceso;
}