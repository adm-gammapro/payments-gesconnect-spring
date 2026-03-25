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
     * @return {@link Void}
     */
    @PostMapping("/validar")
    public ResponseEntity<Void> validar(@Valid @RequestBody CambioEstadoRequestDto req) {
        flujoService.validar(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Envía a autorización y cambia estado a PENDIENTE_AUTORIZACION.
     *
     * @param req datos de la solicitud y auditoría
     * @return {@link Void}
     */
    @PostMapping("/enviar-autorizacion")
    public ResponseEntity<Void> enviarAutorizacion(@Valid @RequestBody CambioEstadoRequestDto req) {
        flujoService.enviarAutorizacion(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Autoriza y cambia estado a AUTORIZADO.
     *
     * @param req datos de la solicitud y auditoría
     * @return {@link Void}
     */
    @PostMapping("/autorizar")
    public ResponseEntity<Void> autorizar(@Valid @RequestBody CambioEstadoRequestDto req) {
        flujoService.autorizar(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Ejecuta y cambia estado a PROCESADO_TOTAL.
     *
     * @param req datos de la solicitud y auditoría
     * @return {@link Void}
     */
    @PostMapping("/ejecutar")
    public ResponseEntity<Void> ejecutar(@Valid @RequestBody CambioEstadoRequestDto req) {
        flujoService.ejecutar(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Observa y cambia estado a OBSERVADO, registrando observación.
     *
     * @param req datos de la solicitud, auditoría y observación
     * @return {@link Void}
     */
    @PostMapping("/observar")
    public ResponseEntity<Void> observar(@Valid @RequestBody ObservacionCambioEstadoRequestDto req) {
        flujoService.observar(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Anula y cambia estado a ANULADO, registrando observación.
     *
     * @param req datos de la solicitud, auditoría y observación
     * @return {@link Void}
     */
    @PostMapping("/anular")
    public ResponseEntity<Void> anular(@Valid @RequestBody ObservacionCambioEstadoRequestDto req) {
        flujoService.anular(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/list-page-solicitud")
    public ResponseEntity<Page<SolicitudResponseDto>> listarCuentasPage(@RequestBody(required = false) SolicitudSearchDto solicitudSearch,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),  // Asegura que el número de página no sea negativo
                Math.max(size, 1)   // Asegura que el tamaño de página sea al menos 1
        );

        return ResponseEntity.ok(solicitudService.getPageSolicitudes(solicitudSearch, pageable));
    }
}