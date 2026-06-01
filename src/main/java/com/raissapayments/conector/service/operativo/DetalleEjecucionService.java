package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.DetalleEjecucionResponseDto;
import org.springframework.data.domain.Page;

public interface DetalleEjecucionService {
    DetalleEjecucionResponseDto registrar(DetalleEjecucionRequestDto dto);
    DetalleEjecucionResponseDto actualizar(Long id, DetalleEjecucionRequestDto dto);
    DetalleEjecucionResponseDto buscarPorId(Long id);
    Page<DetalleEjecucionResponseDto> buscarPaginado(DetalleEjecucionSearchDto filtros);
}
