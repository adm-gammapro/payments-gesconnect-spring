package com.raissapayments.conector.config.rest.exceptionhandler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que captura las exceptiones producidas en tiempo de ejecución y genera una respuesta adecuada
 *
 * @since 1.0.0
 */
@ControllerAdvice
@Slf4j
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("Argumento ilegal", ex);
        return handleExceptionInternal(ex, new RestResponseExceptionDto(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = IndexOutOfBoundsException.class)
    protected ResponseEntity<Object> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex, WebRequest request) {
        log.error("Índice fuera de rango", ex);
        return handleExceptionInternal(ex, new RestResponseExceptionDto(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleExceptionGeneric(Exception ex, WebRequest request) {
        log.error("Error no controlado", ex);
        return handleExceptionInternal(ex, new RestResponseExceptionDto(ex), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        log.error("Error de integridad de datos", ex);

        return handleExceptionInternal(ex, new RestResponseExceptionDto("Error inesperado. Por favor, contáctese con sistemas."), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        log.error("Error no controlado", ex);
        return handleExceptionInternal(ex, new RestResponseExceptionDto(ex), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        return buildValidationErrorExceptionResponse(ex, ex.getBindingResult(), request);
    }

    /**
     * Construye una respuesta de error de validación a partir de una excepción de validación
     *
     * @param ex Excepción de validación
     * @param bindingResult Resultado de la validación
     * @param request Petición web
     * @return Respuesta de error de validación
     */
    private ResponseEntity<Object> buildValidationErrorExceptionResponse(Exception ex, BindingResult bindingResult, WebRequest request) {
        List<MethodArgumentNotValidExceptionResponseDto> validationErrors = bindingResult.getAllErrors().stream()
                .map(MethodArgumentNotValidExceptionResponseDto::new).collect(Collectors.toList());

        return handleExceptionInternal(ex, new RestResponseExceptionDto(validationErrors, ex.getClass().getSimpleName()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
