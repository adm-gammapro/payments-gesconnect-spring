package com.raissapayments.conector.service.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.TipoPagoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoPagoService {
    TipoPagoResponseDto create(TipoPagoRequestDto requestDto);
    TipoPagoResponseDto update(TipoPagoRequestDto requestDto);
    TipoPagoResponseDto delete(TipoPagoRequestDto requestDto); // lógico
    TipoPagoResponseDto get(Long codigo);
    Page<TipoPagoResponseDto> listPage(String filtroDescripcion, Pageable pageable);
}
