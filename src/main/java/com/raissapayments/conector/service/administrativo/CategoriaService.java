package com.raissapayments.conector.service.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDto create(CategoriaRequestDto requestDto);
    CategoriaResponseDto update(CategoriaRequestDto requestDto);
    CategoriaResponseDto delete(CategoriaRequestDto requestDto); // lógico
    CategoriaResponseDto get(Long codigo);
    Page<CategoriaResponseDto> listPage(String filtroDescripcion, Pageable pageable);
}
