package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ReglaSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ReglaResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReglaService {
    ReglaResponseDto create(ReglaRequestDto requestDto);
    ReglaResponseDto update(ReglaRequestDto requestDto);
    ReglaResponseDto delete(ReglaRequestDto requestDto);
    ReglaResponseDto get(Long codigo);
    Page<ReglaResponseDto> listPage(ReglaSearchDto searchDto);
    List<ReglaResponseDto> listReglas();
}