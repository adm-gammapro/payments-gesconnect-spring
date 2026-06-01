package com.raissapayments.conector.rest.commons;

import com.raissapayments.conector.domain.dto.commons.EstadoSolicitudDto;
import com.raissapayments.conector.service.commons.GeneralService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/general")
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class GeneralRest {
    private final GeneralService generalService;

    @GetMapping("/estados/activos")
    public ResponseEntity<List<EstadoSolicitudDto>> listarEstadosActivos() {
        try {
            log.info("Petición GET para listar estados activos");
            return ResponseEntity.ok(generalService.listarEstadosActivos());
        } catch (Exception e) {
            log.error("Error al procesar la petición: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}