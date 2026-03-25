package com.raissapayments.conector.domain.mapper.commons;

import com.raissa.comun.enums.commons.EstadoRegistroEnum;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MapperUtil {
    /**
     * Método auxiliar para convertir String a EstadoRegistroEnum.
     */
    @Named("mapStringToEstadoRegistroEnum")
    public EstadoRegistroEnum mapStringToEstadoRegistroEnum(String estado) {
        return estado != null ? EstadoRegistroEnum.fromValor(estado) : null;
    }

    /**
     * Método personalizado para convertir LocalDateTime a String
     */
    @Named("mapLocalDateTimeToString")
    public String mapLocalDateTimeToString(LocalDateTime date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
    }

    /**
     * Método personalizado para convertir LocalDate a String
     */
    @Named("mapLocalDateToString")
    public String mapLocalDateToString(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : null;
    }

    /**
     * Método personalizado para convertir String a LocalDate
     */
    @Named("emptyToNull")
    public LocalDate emptyToNull(String s) {
        if (s == null || s.isEmpty()) return null;
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}