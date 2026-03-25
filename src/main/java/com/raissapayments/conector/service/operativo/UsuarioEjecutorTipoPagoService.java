package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.UsuarioEjecutorTipoPagoDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioEjecutorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioEjecutorTipoPagoResponseDto;

import java.util.List;

public interface UsuarioEjecutorTipoPagoService {
    UsuarioEjecutorTipoPagoResponseDto crear(UsuarioEjecutorTipoPagoRequestDto request);
    List<UsuarioEjecutorTipoPagoResponseDto> listarActivos();
    void eliminarLogico(UsuarioEjecutorTipoPagoDeleteRequestDto request);
}