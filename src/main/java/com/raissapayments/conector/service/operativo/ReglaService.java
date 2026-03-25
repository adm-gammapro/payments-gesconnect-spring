package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ReglaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReglaService {
    ReglaResponseDto create(ReglaRequestDto requestDto);
    ReglaResponseDto update(ReglaRequestDto requestDto);
    ReglaResponseDto delete(ReglaRequestDto requestDto);
    ReglaResponseDto get(Long codigo);
    Page<ReglaResponseDto> listPage(String filtroDescripcion, Pageable pageable);
}