package com.raissapayments.conector.rest.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoSearchDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import com.raissapayments.conector.domain.dto.administrativo.response.TipoPagoResponseDto;
import com.raissapayments.conector.service.administrativo.TipoPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tipo-pago")
@RequiredArgsConstructor
public class TipoPagoRest {
    private final TipoPagoService tipoPagoService;

    @PostMapping("/create-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> createTipoPago(@Valid @RequestBody TipoPagoRequestDto requestDto) {
        log.info("Registrando tipo de pago");
        TipoPagoResponseDto resp = tipoPagoService.create(requestDto);
        log.info("Tipo de pago registrado correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/update-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> updateTipoPago(@Valid @RequestBody TipoPagoRequestDto requestDto) {
        log.info("Actualizando tipo de pago");
        TipoPagoResponseDto resp = tipoPagoService.update(requestDto);
        log.info("Tipo de pago actualizado correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/delete-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> deleteTipoPago(@RequestBody TipoPagoRequestDto requestDto) {
        return ResponseEntity.ok(tipoPagoService.delete(requestDto));
    }

    @PostMapping("/list-page-tipo-pago")
    public ResponseEntity<Page<TipoPagoResponseDto>> getPageTipoPago(@RequestBody TipoPagoSearchDto searchDto) {
        return ResponseEntity.ok(tipoPagoService.listPage(searchDto));
    }

    @PostMapping("/get-tipo-pago")
    public ResponseEntity<TipoPagoResponseDto> getTipoPago(@RequestBody TipoPagoRequestDto requestDto) {
        return ResponseEntity.ok(tipoPagoService.get(requestDto.getCodigo()));
    }

    @PostMapping("/list-tipo-pago")
    public ResponseEntity<List<TipoPagoResponseDto>> list() {
        return ResponseEntity.ok(tipoPagoService.listTipoPago());
    }
}