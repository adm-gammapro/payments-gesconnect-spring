package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.CabeceraEjecucionResponseDto;
import org.springframework.data.domain.Page;

public interface CabeceraEjecucionService {
    CabeceraEjecucionResponseDto registrar(CabeceraEjecucionRequestDto dto);
    CabeceraEjecucionResponseDto actualizar(Long id, CabeceraEjecucionRequestDto dto);
    CabeceraEjecucionResponseDto buscarPorId(Long id);
    Page<CabeceraEjecucionResponseDto> buscarPaginado(CabeceraEjecucionSearchDto filtros);
}
