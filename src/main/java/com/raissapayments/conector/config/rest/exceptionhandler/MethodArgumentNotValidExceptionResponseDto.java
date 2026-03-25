package com.raissapayments.conector.config.rest.exceptionhandler;

import lombok.Data;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * Clase que representa la respuesta de una excepción de validación
 *
 * @since 1.0.0
 */
@Data
public class MethodArgumentNotValidExceptionResponseDto {
    private String field;
    private String message;

    public MethodArgumentNotValidExceptionResponseDto(ObjectError error) {
        this.field = error.getObjectName() +
                (error instanceof FieldError
                        ? ("." + ((FieldError) error).getField())
                        : ""
                );
        this.message = error.getDefaultMessage();
    }
}
