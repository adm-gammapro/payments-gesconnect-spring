package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.CambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionCambioEstadoRequestDto;

public interface SolicitudFlujoService {
    /**
     * Ejecuta validaciones y cambia estado a VALIDADO
     *
     * @param req Datos de solicitud
     */
    void validar(CambioEstadoRequestDto req);

    /**
     * Envía a autorización y cambia estado a PENDIENTE_AUTORIZACION.
     *
     * @param req datos de solicitud y auditoría
     */
    void enviarAutorizacion(CambioEstadoRequestDto req);

    /**
     * Autoriza y cambia estado a AUTORIZADO.
     *
     * @param req datos de solicitud y auditoría
     */
    void autorizar(CambioEstadoRequestDto req);

    /**
     * Ejecuta y cambia estado a PROCESADO_TOTAL.
     *
     * @param req datos de solicitud y auditoría
     */
    void ejecutar(CambioEstadoRequestDto req);

    /**
     * Observa y cambia estado a OBSERVADO, registrando observación.
     *
     * @param req datos de solicitud, auditoría y observación
     */
    void observar(ObservacionCambioEstadoRequestDto req);

    /**
     * Anula y cambia estado a ANULADO, registrando observación.
     *
     * @param req datos de solicitud, auditoría y observación
     */
    void anular(ObservacionCambioEstadoRequestDto req);
}