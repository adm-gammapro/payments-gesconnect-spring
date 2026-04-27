package com.raissapayments.conector.domain.dto.administrativo.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VinculoCategoriaUsuarioResponseDto {
    private Long idCategoria;
    private List<String> usuariosDisponibles;
    private List<String> usuariosVinculados;
}