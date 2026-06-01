package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.DetalleEjecucionResponseDto;
import com.raissapayments.conector.service.operativo.DetalleEjecucionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/detalle-ejecucion")
@RequiredArgsConstructor
public class DetalleEjecucionRest {
    private final DetalleEjecucionService service;

    @PostMapping
    public ResponseEntity<DetalleEjecucionResponseDto> registrar(@RequestBody @Valid DetalleEjecucionRequestDto dto) {
        log.info("POST /api/detalle-ejecucion");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleEjecucionResponseDto> actualizar(@PathVariable Long id,
                                                                  @RequestBody @Valid DetalleEjecucionRequestDto dto) {
        log.info("PUT /api/detalle-ejecucion/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleEjecucionResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/buscar")
    public ResponseEntity<Page<DetalleEjecucionResponseDto>> buscarPaginado(@RequestBody(required = false) DetalleEjecucionSearchDto filtros) {
        log.info("POST /api/detalle-ejecucion/buscar");
        return ResponseEntity.ok(service.buscarPaginado(filtros));
    }
}
