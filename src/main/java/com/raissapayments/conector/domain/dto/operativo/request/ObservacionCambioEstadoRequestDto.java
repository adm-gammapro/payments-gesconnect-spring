package com.raissapayments.conector.domain.dto.operativo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Valid
public class ObservacionCambioEstadoRequestDto extends CambioEstadoRequestDto {
    @NotBlank
    @Size(max = 2000)
    private String descripcionObservacion;

    private String eventoObservacion;

    private String usuarioObservacion;
}