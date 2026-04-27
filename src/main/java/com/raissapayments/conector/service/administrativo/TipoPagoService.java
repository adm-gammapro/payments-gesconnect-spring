package com.raissapayments.conector.service.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoSearchDto;
import com.raissapayments.conector.domain.dto.administrativo.response.TipoPagoResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TipoPagoService {
    TipoPagoResponseDto create(TipoPagoRequestDto requestDto);
    TipoPagoResponseDto update(TipoPagoRequestDto requestDto);
    TipoPagoResponseDto delete(TipoPagoRequestDto requestDto);
    TipoPagoResponseDto get(Long codigo);
    Page<TipoPagoResponseDto> listPage(TipoPagoSearchDto searchDto);
    List<TipoPagoResponseDto> listTipoPago();
}
