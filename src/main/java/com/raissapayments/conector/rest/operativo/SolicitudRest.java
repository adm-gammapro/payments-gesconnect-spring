package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.CambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionCambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.SolicitudSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import com.raissapayments.conector.service.operativo.SolicitudFlujoService;
import com.raissapayments.conector.service.operativo.SolicitudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/flujo-solicitud")
@RequiredArgsConstructor
public class SolicitudRest {
    private final SolicitudFlujoService flujoService;
    private final SolicitudService solicitudService;

    /**
     * Ejecuta validaciones y cambia estado a VALIDADO
     *
     * @param req Datos de la solicitud
     * @return {@link Long}
     */
    @PostMapping("/validar")
    public ResponseEntity<Long> validar(@Valid @RequestBody CambioEstadoRequestDto req) {
        return ResponseEntity.ok(flujoService.validar(req));
    }

    /**
     * Envía a autorización y cambia estado a PENDIENTE_AUTORIZACION.
     *
     * @param req datos de la solicitud y auditoría
     * @return {@link Long}
     */
    @PostMapping("/enviar-autorizacion")
    public ResponseEntity<Long> enviarAutorizacion(@Valid @RequestBody CambioEstadoRequestDto req) {
        return ResponseEntity.ok(flujoService.enviarAutorizacion(req));
    }

    /**
     * Autoriza y cambia estado a AUTORIZADO.
     *
     * @param req datos de la solicitud y auditoría
     * @return {@link Long}
     */
    @PostMapping("/autorizar")
    public ResponseEntity<Long> autorizar(@Valid @RequestBody CambioEstadoRequestDto req) {
        return ResponseEntity.ok(flujoService.autorizar(req));
    }

    /**
     * Ejecuta y cambia estado a PROCESADO_TOTAL.
     *
     * @param req datos de la solicitud y auditoría
     * @return {@link Long}
     */
    @PostMapping("/ejecutar")
    public ResponseEntity<Long> ejecutar(@Valid @RequestBody CambioEstadoRequestDto req) {
        return ResponseEntity.ok(flujoService.ejecutar(req));
    }

    /**
     * Ejecuta y cambia estado a PROCESADO_TOTAL.
     *
     * @param req datos de la solicitud y auditoría
     * @return {@link Long}
     */
    @PostMapping("/reprocesar")
    public ResponseEntity<Long> reprocesar(@Valid @RequestBody CambioEstadoRequestDto req) {
        return ResponseEntity.ok(flujoService.ejecutar(req));
    }

    /**
     * Observa y cambia estado a OBSERVADO, registrando observación.
     *
     * @param req datos de la solicitud, auditoría y observación
     * @return {@link Long}
     */
    @PostMapping("/observar")
    public ResponseEntity<Long> observar(@Valid @RequestBody ObservacionCambioEstadoRequestDto req) {
        return ResponseEntity.ok(flujoService.observar(req));
    }

    /**
     * Anula y cambia estado a ANULADO, registrando observación.
     *
     * @param req datos de la solicitud, auditoría y observación
     * @return {@link Long}
     */
    @PostMapping("/anular")
    public ResponseEntity<Long> anular(@Valid @RequestBody ObservacionCambioEstadoRequestDto req) {
        return ResponseEntity.ok(flujoService.anular(req));
    }

    @PostMapping("/list-page-solicitud")
    public ResponseEntity<Page<SolicitudResponseDto>> listarCuentasPage(@RequestBody(required = false) SolicitudSearchDto solicitudSearch) {
        return ResponseEntity.ok(solicitudService.getPageSolicitudes(solicitudSearch));
    }
}