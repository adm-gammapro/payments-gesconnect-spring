package com.raissapayments.conector.service.commons;

import com.raissapayments.conector.domain.dto.commons.EstadoSolicitudDto;

import java.util.List;

public interface GeneralService {
    List<EstadoSolicitudDto> listarEstadosActivos();
}
