package com.raissapayments.conector.service.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaSearchDto;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaUsuarioRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.VinculoCategoriaUsuarioRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import com.raissapayments.conector.domain.dto.administrativo.response.VinculoCategoriaUsuarioResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoriaService {
    CategoriaResponseDto create(CategoriaRequestDto requestDto);
    CategoriaResponseDto update(CategoriaRequestDto requestDto);
    CategoriaResponseDto delete(CategoriaRequestDto requestDto); // lógico
    CategoriaResponseDto get(Long codigo);
    Page<CategoriaResponseDto> listPage(CategoriaSearchDto searchDto);

    List<CategoriaResponseDto> listCategorias();

    VinculoCategoriaUsuarioResponseDto listVinculoCategoriaUsuario(VinculoCategoriaUsuarioRequestDto requestDto);

    Boolean vincularCategoriaUsuario(CategoriaUsuarioRequestDto requestDto);

    Boolean desvincularCategoriaUsuario(CategoriaUsuarioRequestDto requestDto);
}
