package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ReglaSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ConfiguracionReglaResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ReglaResponseDto;
import com.raissapayments.conector.service.operativo.ReglaService;
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
@RequestMapping("/regla")
@RequiredArgsConstructor
public class ReglaRest {
    private final ReglaService reglaService;

    @PostMapping("/create-regla")
    public ResponseEntity<ReglaResponseDto> createRegla(@Valid @RequestBody ReglaRequestDto requestDto) {
        log.info("Registrando regla");
        ReglaResponseDto resp = reglaService.create(requestDto);
        log.info("Regla registrada correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/update-regla")
    public ResponseEntity<ReglaResponseDto> updateRegla(@Valid @RequestBody ReglaRequestDto requestDto) {
        log.info("Actualizando regla");
        ReglaResponseDto resp = reglaService.update(requestDto);
        log.info("Regla actualizada correctamente");
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/delete-regla")
    public ResponseEntity<ReglaResponseDto> deleteRegla(@RequestBody ReglaRequestDto requestDto) {
        return ResponseEntity.ok(reglaService.delete(requestDto));
    }

    @PostMapping("/list-page-regla")
    public ResponseEntity<Page<ReglaResponseDto>> getPageRegla(@RequestBody ReglaSearchDto searchDto) {
        return ResponseEntity.ok(reglaService.listPage(searchDto));
    }

    @PostMapping("/get-regla")
    public ResponseEntity<ReglaResponseDto> getRegla(@RequestBody ReglaRequestDto requestDto) {
        return ResponseEntity.ok(reglaService.get(requestDto.getCodigo()));
    }

    @PostMapping("/list-regla")
    public ResponseEntity<List<ReglaResponseDto>> list() {
        return ResponseEntity.ok(reglaService.listReglas());
    }
}
