package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.general.dto.ResponseDTO;
import com.raissa.comun.general.service.AbstractService;
import com.raissa.comun.util.Constante;
import com.raissa.comun.util.ConstanteError;
import com.raissapayments.conector.domain.dto.operativo.request.LoginAlfinRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConfirmaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConsultaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.LoginResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.SaldoResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.GroupConfirmaTransResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.GroupConsultaTransResponseDto;
import com.raissapayments.conector.exception.operativo.EmptyResponseException;
import com.raissapayments.conector.exception.operativo.ErrorControladoException;
import com.raissapayments.conector.service.operativo.ApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiServiceImpl extends AbstractService implements ApiService {
    private final RestTemplate restTemplate;

    @Value("${apisraissa.api.url}") String apiUrl;

    @Override
    public ResponseDTO<?> login(LoginAlfinRequestDto loginDto) {
        try {
            String url = apiUrl + "/api/bff/login";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(Constante.X_API_KEY, loginDto.getApiKey());

            Map<String, String> body = new HashMap<>();
            body.put("username", loginDto.getUsername());
            body.put("password", loginDto.getPassword());
            body.put("provider", loginDto.getProvider());

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            log.info("Autenticando en: {}", url);

            ResponseEntity<LoginResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    LoginResponseDto.class
            );

            LoginResponseDto loginResponseDto = response.getBody();
            if (_isEmpty(loginResponseDto)) {
                throw new EmptyResponseException(ConstanteError.MENSAJE_ERROR_RESPUESTA_VACIA);
            }

            if (loginResponseDto != null && _equiv(loginResponseDto.getStatus(), Constante.KEY_ERROR_CODE)) {
                throw new ErrorControladoException("Error de autenticación en el servidor de APIS");
            }

            log.info("Obtención de saldos exitosa - status: {}", loginResponseDto != null ? loginResponseDto.getStatus() : Constante.SIN_STATUS);

            if (loginResponseDto != null && _isEmpty(loginResponseDto.getKey())) {
                throw new ErrorControladoException("TransactionId no recibido en la autenticación");
            }

            log.info("Autenticación exitosa - TransactionId: {}, Usuario: {}",
                    loginResponseDto != null ? loginResponseDto.getKey() : "Sin transactionId", loginDto.getUsername());

            return ResponseDTO.builder()
                    .status(true)
                    .message(loginResponseDto != null ? loginResponseDto.getStatus() : Constante.SIN_STATUS)
                    .body(loginResponseDto)
                    .build();
        } catch (ErrorControladoException e) {
            log.error(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION_API_KEY, loginDto.getApiKey(), e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION + e.getMessage())
                    .body(null)
                    .build();
        } catch (Exception e) {
            log.error(ConstanteError.MENSAJE_ERROR_INESPERADO_API_KEY, loginDto.getApiKey(), e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_INESPERADO + e.getMessage())
                    .body(null)
                    .build();
        }
    }

    @Override
    public ResponseDTO<?> saldos(String key, String apiKey, String usuario, String numeroCuenta) {
        try {
            String url = apiUrl + "/api/bff/account/" + key;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(Constante.X_API_KEY, apiKey);

            Map<String, String> body = new HashMap<>();
            body.put("codigoUsuario", usuario);
            body.put("numeroCuenta", numeroCuenta);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            log.info("Autenticando en: {}", url);

            ResponseEntity<SaldoResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    SaldoResponseDto.class
            );

            SaldoResponseDto saldoResponse = response.getBody();
            if (_isEmpty(saldoResponse)) {
                throw new ErrorControladoException(ConstanteError.MENSAJE_ERROR_RESPUESTA_VACIA);
            }

            if (saldoResponse != null && _equiv(saldoResponse.getStatus(), Constante.KEY_ERROR_CODE)) {
                throw new ErrorControladoException("Error al obtener saldos de server de APIS");
            }

            log.info("Obtención de saldos exitosa - status: {}", saldoResponse != null ? saldoResponse.getStatus() : Constante.SIN_STATUS);

            return ResponseDTO.builder()
                    .status(true)
                    .message(saldoResponse != null ? saldoResponse.getStatus() : Constante.SIN_STATUS)
                    .body(saldoResponse)
                    .build();
        } catch (ErrorControladoException e) {
            log.error(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION_API_KEY, apiKey, e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION + e.getMessage())
                    .body(null)
                    .build();
        } catch (Exception e) {
            log.error(ConstanteError.MENSAJE_ERROR_INESPERADO_API_KEY, apiKey, e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_INESPERADO + e.getMessage())
                    .body(null)
                    .build();
        }
    }

    @Override
    public ResponseDTO<?> consultaTranferencia(String key,
                                               String apiKey,
                                               GroupConsultaTransBffRequestDto consultas) {
        try {
            String url = apiUrl + "/api/bff/consulta-transferencia/" + key;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(Constante.X_API_KEY, apiKey);

            HttpEntity<GroupConsultaTransBffRequestDto> request = new HttpEntity<>(consultas, headers);

            log.info("Consultando en: {}", url);

            ResponseEntity<GroupConsultaTransResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    GroupConsultaTransResponseDto.class
            );

            GroupConsultaTransResponseDto consultaResponse = response.getBody();
            if (_isEmpty(consultaResponse)) {
                throw new ErrorControladoException(ConstanteError.MENSAJE_ERROR_RESPUESTA_VACIA);
            }

            if (consultaResponse != null && _equiv(consultaResponse.getStatus(), Constante.KEY_ERROR_CODE)) {
                throw new ErrorControladoException("Respuesta con error: " + consultaResponse.getMessage());
            }

            log.info("Consulta de transferencias exitosa - status: {}", consultaResponse != null ? consultaResponse.getStatus() : Constante.SIN_STATUS);

            return ResponseDTO.builder()
                    .status(true)
                    .message(consultaResponse != null ? consultaResponse.getStatus() : Constante.SIN_STATUS)
                    .body(consultaResponse)
                    .build();
        } catch (ErrorControladoException e) {
            log.error(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION_API_KEY, apiKey, e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION + e.getMessage())
                    .body(null)
                    .build();
        } catch (Exception e) {
            log.error(ConstanteError.MENSAJE_ERROR_INESPERADO_API_KEY, apiKey, e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_INESPERADO + e.getMessage())
                    .body(null)
                    .build();
        }
    }

    @Override
    public ResponseDTO<?> confirmacionTranferencia(String key,
                                                   String apiKey,
                                                   GroupConfirmaTransBffRequestDto confirmaciones) {
        try {
            String url = apiUrl + "/api/bff/confirma-transferencia/" + key;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(Constante.X_API_KEY, apiKey);

            HttpEntity<GroupConfirmaTransBffRequestDto> request = new HttpEntity<>(confirmaciones, headers);

            log.info("Confirmando en: {}", url);

            ResponseEntity<GroupConfirmaTransResponseDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    GroupConfirmaTransResponseDto.class
            );

            GroupConfirmaTransResponseDto confirmacionResponse = response.getBody();
            if (_isEmpty(confirmacionResponse)) {
                throw new ErrorControladoException(ConstanteError.MENSAJE_ERROR_RESPUESTA_VACIA);
            }

            if (confirmacionResponse != null && _equiv(confirmacionResponse.getStatus(), Constante.KEY_ERROR_CODE)) {
                throw new ErrorControladoException("Respuesta con error: " + confirmacionResponse.getMessage());
            }

            log.info("Confirmacion de transferencias exitosa - status: {}", confirmacionResponse != null ? confirmacionResponse.getStatus() : Constante.SIN_STATUS);

            return ResponseDTO.builder()
                    .status(true)
                    .message(confirmacionResponse != null ? confirmacionResponse.getStatus() : Constante.SIN_STATUS)
                    .body(confirmacionResponse)
                    .build();
        } catch (ErrorControladoException e) {
            log.error(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION_API_KEY, apiKey, e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_ERROR_VALIDACION + e.getMessage())
                    .body(null)
                    .build();
        } catch (Exception e) {
            log.error(ConstanteError.MENSAJE_ERROR_INESPERADO_API_KEY, apiKey, e.getMessage());

            return ResponseDTO.builder()
                    .status(false)
                    .message(ConstanteError.MENSAJE_ERROR_INESPERADO + e.getMessage())
                    .body(null)
                    .build();
        }
    }
}
