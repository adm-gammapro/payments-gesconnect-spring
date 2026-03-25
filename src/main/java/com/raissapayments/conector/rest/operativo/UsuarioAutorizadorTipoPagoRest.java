package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.UsuarioAutorizadorTipoPagoDeleteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioAutorizadorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioAutorizadorTipoPagoResponseDto;
import com.raissapayments.conector.service.operativo.UsuarioAutorizadorTipoPagoService;
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
@RequestMapping("/usuario-autorizador-tipo-pago")
@RequiredArgsConstructor
public class UsuarioAutorizadorTipoPagoRest {
    private final UsuarioAutorizadorTipoPagoService service;

    @PostMapping("/create")
    public ResponseEntity<UsuarioAutorizadorTipoPagoResponseDto> crear(@Valid @RequestBody UsuarioAutorizadorTipoPagoRequestDto request) {
        return ResponseEntity.ok(service.crear(request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UsuarioAutorizadorTipoPagoResponseDto>> listar() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> eliminar(@Valid @RequestBody UsuarioAutorizadorTipoPagoDeleteRequestDto request) {
        service.eliminarLogico(request);
        return ResponseEntity.noContent().build();
    }
}