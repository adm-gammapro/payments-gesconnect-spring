package com.raissapayments.conector.domain.dto.administrativo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CategoriaUsuarioRequestDto {
    private Long idCategoria;
    private List<String> usernames;

    @NotNull
    private LocalDateTime fechaAuditoria;

    @NotBlank
    @Size(max = 15)
    private String usuarioAuditoria;

    @NotBlank @Size(max = 30)
    private String terminalAuditoria;

    @NotBlank @Size(max = 20)
    private String ipAuditoria;
}
