package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.UsuarioEjecutorTipoPagoDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioEjecutorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioEjecutorTipoPagoResponseDto;
import com.raissapayments.conector.service.operativo.UsuarioEjecutorTipoPagoService;
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
@RequestMapping("/usuario-ejecutor-tipo-pago")
@RequiredArgsConstructor
public class UsuarioEjecutorTipoPagoRest {
    private final UsuarioEjecutorTipoPagoService service;

    @PostMapping("/create")
    public ResponseEntity<UsuarioEjecutorTipoPagoResponseDto> crear(@Valid @RequestBody UsuarioEjecutorTipoPagoRequestDto request) {
        return ResponseEntity.ok(service.crear(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UsuarioEjecutorTipoPagoResponseDto>> listar() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> eliminar(@Valid @RequestBody UsuarioEjecutorTipoPagoDeleteRequestDto request) {
        service.eliminarLogico(request);
        return ResponseEntity.noContent().build();
    }
}