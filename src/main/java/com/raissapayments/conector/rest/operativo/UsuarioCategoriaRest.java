package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.UsuarioCategoriaDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioCategoriaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioCategoriaResponseDto;
import com.raissapayments.conector.service.operativo.UsuarioCategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuario-categorias")
@RequiredArgsConstructor
public class UsuarioCategoriaRest {
    private final UsuarioCategoriaService service;

    @PostMapping("/create")
    public ResponseEntity<UsuarioCategoriaResponseDto> crear(@Valid @RequestBody UsuarioCategoriaRequestDto request) {
        return ResponseEntity.ok(service.crear(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UsuarioCategoriaResponseDto>> listar() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> eliminar(@Valid @RequestBody UsuarioCategoriaDeleteRequestDto request) {
        service.eliminarLogico(request);
        return ResponseEntity.noContent().build();
    }
}