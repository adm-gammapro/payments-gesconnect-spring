package com.raissapayments.conector.domain.dto.operativo.response;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCategoriaResponseDto {
    private Long id;
    private String username;
    private Long categoriaId;
    private String categoriaNombre;
    private EstadoRegistroEnum estadoRegistro;
    private String audiFechIns;
}