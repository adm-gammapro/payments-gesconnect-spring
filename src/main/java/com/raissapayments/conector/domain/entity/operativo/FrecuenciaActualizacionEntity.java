package com.raissapayments.conector.domain.entity.operativo;

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
@Table(name = "RAITFREACT", schema = "public")
public class FrecuenciaActualizacionEntity extends Auditoria {
	@Id
    @Column(name = "c_codfre", nullable = false)
    private String codigo;
    
    @Column(name = "c_desfre", nullable = false)
    private String descripcionFrecuencia;
    
    @Column(name = "n_diafre", nullable = false)
    private Integer diasFrecuencia;
}