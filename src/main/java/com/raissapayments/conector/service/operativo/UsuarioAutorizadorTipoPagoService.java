package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.UsuarioAutorizadorTipoPagoDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioAutorizadorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioAutorizadorTipoPagoResponseDto;

import java.util.List;

public interface UsuarioAutorizadorTipoPagoService {
    UsuarioAutorizadorTipoPagoResponseDto crear(UsuarioAutorizadorTipoPagoRequestDto request);
    List<UsuarioAutorizadorTipoPagoResponseDto> listarActivos();
    void eliminarLogico(UsuarioAutorizadorTipoPagoDeleteRequestDto request);
}
