package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.CabeceraEjecucionResponseDto;
import com.raissapayments.conector.service.operativo.CabeceraEjecucionService;
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
@RequestMapping("/api/cabecera-ejecucion")
@RequiredArgsConstructor
public class CabeceraEjecucionRest {
    private final CabeceraEjecucionService service;

    @PostMapping
    public ResponseEntity<CabeceraEjecucionResponseDto> registrar(@RequestBody @Valid CabeceraEjecucionRequestDto dto) {
        log.info("POST /api/cabecera-ejecucion");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CabeceraEjecucionResponseDto> actualizar(@PathVariable Long id,
                                                                   @RequestBody @Valid CabeceraEjecucionRequestDto dto) {
        log.info("PUT /api/cabecera-ejecucion/{}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CabeceraEjecucionResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/buscar")
    public ResponseEntity<Page<CabeceraEjecucionResponseDto>> buscarPaginado(@RequestBody(required = false) CabeceraEjecucionSearchDto filtros) {
        log.info("POST /api/cabecera-ejecucion/buscar");
        return ResponseEntity.ok(service.buscarPaginado(filtros));
    }
}
