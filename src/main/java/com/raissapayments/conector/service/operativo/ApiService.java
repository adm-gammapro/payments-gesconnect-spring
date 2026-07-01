package com.raissapayments.conector.service.operativo;

import com.raissa.comun.general.dto.ResponseDTO;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConfirmaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConsultaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.LoginAlfinRequestDto;

public interface ApiService {
    ResponseDTO<?> login(LoginAlfinRequestDto loginDto);

    ResponseDTO<?> saldos(String key, String apiKey, String usuario, String numeroCuenta);

    ResponseDTO<?> consultaTranferencia(String key,
                                        String apiKey,
                                        GroupConsultaTransBffRequestDto consultas);

    ResponseDTO<?> consultaTranferenciaDetallada(String key,
                                                 String apiKey,
                                                 GroupConsultaTransBffRequestDto consultas);

    ResponseDTO<?> confirmacionTranferencia(String key,
                                            String apiKey,
                                            GroupConfirmaTransBffRequestDto consultas);

    ResponseDTO<?> confirmacionTranferenciaDetallada(String key,
                                                     String apiKey,
                                                     GroupConfirmaTransBffRequestDto confirmaciones);
}