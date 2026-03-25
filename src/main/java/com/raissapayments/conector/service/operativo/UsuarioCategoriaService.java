package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.UsuarioCategoriaDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioCategoriaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioCategoriaResponseDto;

import java.util.List;

public interface UsuarioCategoriaService {
    UsuarioCategoriaResponseDto crear(UsuarioCategoriaRequestDto request);
    List<UsuarioCategoriaResponseDto> listarActivos();
    void eliminarLogico(UsuarioCategoriaDeleteRequestDto request);
}
