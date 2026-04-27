package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.SolicitudSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import org.springframework.data.domain.Page;

public interface SolicitudService {
    Page<SolicitudResponseDto> getPageSolicitudes(SolicitudSearchDto filtro);
}
