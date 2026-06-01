package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.CambioEstadoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionCambioEstadoRequestDto;

public interface SolicitudFlujoService {
    /**
     * Ejecuta validaciones y cambia estado a VALIDADO
     *
     * @param req Datos de solicitud
     */
    Long validar(CambioEstadoRequestDto req) throws Exception;

    /**
     * Envía a autorización y cambia estado a PENDIENTE_AUTORIZACION.
     *
     * @param req datos de solicitud y auditoría
     */
    Long enviarAutorizacion(CambioEstadoRequestDto req);

    /**
     * Autoriza y cambia estado a AUTORIZADO.
     *
     * @param req datos de solicitud y auditoría
     */
    Long autorizar(CambioEstadoRequestDto req);

    /**
     * Ejecuta y cambia estado a PROCESADO_TOTAL.
     *
     * @param req datos de solicitud y auditoría
     */
    Long ejecutar(CambioEstadoRequestDto req) throws Exception;

    /**
     * Observa y cambia estado a OBSERVADO, registrando observación.
     *
     * @param req datos de solicitud, auditoría y observación
     */
    Long observar(ObservacionCambioEstadoRequestDto req);

    /**
     * Anula y cambia estado a ANULADO, registrando observación.
     *
     * @param req datos de solicitud, auditoría y observación
     */
    Long anular(ObservacionCambioEstadoRequestDto req);

    Long enProcesamiento(CambioEstadoRequestDto req);
}