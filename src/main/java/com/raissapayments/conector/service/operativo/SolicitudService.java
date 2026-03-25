package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.SolicitudSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface SolicitudService {
    Page<SolicitudResponseDto> getPageSolicitudes(SolicitudSearchDto filtro,
                                                  Pageable pageable);

    SolicitudEntity actualizarEstadoSolicitud(Long solicitudId, String nuevoEstadoId);
}
