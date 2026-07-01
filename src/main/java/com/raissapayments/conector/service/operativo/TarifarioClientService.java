package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.RegistrarConsumoRequestDto;

public interface TarifarioClientService {
    void registrarConsumo(RegistrarConsumoRequestDto request);
}