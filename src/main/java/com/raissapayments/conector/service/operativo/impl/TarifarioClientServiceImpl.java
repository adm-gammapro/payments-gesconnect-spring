package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.RegistrarConsumoRequestDto;
import com.raissapayments.conector.service.commons.ConstanteSistemaService;
import com.raissapayments.conector.service.operativo.TarifarioClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TarifarioClientServiceImpl implements TarifarioClientService {
    private final RestTemplate restTemplate;
    private final ConstanteSistemaService constanteSistemaService;

    @Value("${payments.api.url}")
    private String extranetUrl;

    @Async("tarifarioExecutor")
    @Override
    public void registrarConsumo(RegistrarConsumoRequestDto request) {
        try {
            String keyId = constanteSistemaService.obtenerValor(Constante.KEY_ORG);
            String token = constanteSistemaService.obtenerValor(Constante.INTERNAL_TOKEN_ORG);

            HttpHeaders headers = new HttpHeaders();

            headers.add("X-KEY-ID", keyId);
            headers.add("X-INTERNAL-TOKEN", token);

            HttpEntity<RegistrarConsumoRequestDto> entity = new HttpEntity<>(request, headers);

            restTemplate.exchange(extranetUrl + "/internal/tarifario/consumo",
                    HttpMethod.POST,
                    entity,
                    Void.class
            );
        } catch (Exception e) {
            log.error("Error registrando consumo tarifario", e
            );
        }
    }
}
