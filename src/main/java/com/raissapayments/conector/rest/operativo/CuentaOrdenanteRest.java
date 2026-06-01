package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.CuentaOrdenanteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.CuentaOrdenanteSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.CuentaOrdenanteResponseDto;
import com.raissapayments.conector.service.operativo.CuentaOrdenanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cuenta-ordenante")
@RequiredArgsConstructor
public class CuentaOrdenanteRest {
    private final CuentaOrdenanteService cuentaOrdenanteService;

    @PostMapping("/create")
    public ResponseEntity<CuentaOrdenanteResponseDto> create(@Valid @RequestBody CuentaOrdenanteRequestDto requestDto) throws Exception {
        log.info("Registrando cuenta ordenante");
        CuentaOrdenanteResponseDto resp = cuentaOrdenanteService.create(requestDto);
        log.info("Cuenta ordenante registrada correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/update")
    public ResponseEntity<CuentaOrdenanteResponseDto> update(@Valid @RequestBody CuentaOrdenanteRequestDto requestDto) throws Exception {
        log.info("Actualizando cuenta ordenante");
        CuentaOrdenanteResponseDto resp = cuentaOrdenanteService.update(requestDto);
        log.info("Cuenta ordenante actualizada correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/delete")
    public ResponseEntity<CuentaOrdenanteResponseDto> delete(@RequestBody CuentaOrdenanteRequestDto requestDto) {
        log.info("Eliminando cuenta ordenante - ID: {}", requestDto.getCodigo());
        return ResponseEntity.ok(cuentaOrdenanteService.delete(requestDto));
    }

    @PostMapping("/get")
    public ResponseEntity<CuentaOrdenanteResponseDto> get(@RequestBody CuentaOrdenanteRequestDto requestDto) throws Exception {
        return ResponseEntity.ok(cuentaOrdenanteService.get(requestDto.getCodigo()));
    }

    @PostMapping("/list-page")
    public ResponseEntity<Page<CuentaOrdenanteResponseDto>> getPage(@RequestBody CuentaOrdenanteSearchDto searchDto) {
        return ResponseEntity.ok(cuentaOrdenanteService.listPage(searchDto));
    }

    @PostMapping("/list")
    public ResponseEntity<List<CuentaOrdenanteResponseDto>> list() {
        return ResponseEntity.ok(cuentaOrdenanteService.listCuentaOrdenante());
    }
}
