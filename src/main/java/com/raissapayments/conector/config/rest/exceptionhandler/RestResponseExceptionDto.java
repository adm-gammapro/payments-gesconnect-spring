package com.raissapayments.conector.config.rest.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para enviar información de excepciones en respuestas HTTP
 *
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class RestResponseExceptionDto {
    private Object message;
    private String exceptionCode;

    public RestResponseExceptionDto(Exception ex) {
        this.message = ex.getMessage();
        this.exceptionCode = ex.getClass().getSimpleName();
    }

    public RestResponseExceptionDto(String message) {
        this.message = message;
        this.exceptionCode = "500";
    }
}
