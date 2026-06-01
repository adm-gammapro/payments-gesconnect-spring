package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.CuentaOrdenanteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.CuentaOrdenanteSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.CuentaOrdenanteResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CuentaOrdenanteService {
    CuentaOrdenanteResponseDto create(CuentaOrdenanteRequestDto requestDto) throws Exception;

    CuentaOrdenanteResponseDto update(CuentaOrdenanteRequestDto requestDto) throws Exception;

    CuentaOrdenanteResponseDto delete(CuentaOrdenanteRequestDto requestDto);

    CuentaOrdenanteResponseDto get(Long codigo) throws Exception;

    Page<CuentaOrdenanteResponseDto> listPage(CuentaOrdenanteSearchDto searchDto);

    List<CuentaOrdenanteResponseDto> listCuentaOrdenante();
}