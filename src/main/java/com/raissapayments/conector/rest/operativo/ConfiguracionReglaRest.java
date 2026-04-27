package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ConfiguracionReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ConfiguracionReglaSearchDto;
import com.raissapayments.conector.domain.dto.operativo.response.ConfiguracionReglaResponseDto;
import com.raissapayments.conector.service.operativo.ConfiguracionReglaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracion-regla")
@RequiredArgsConstructor
public class ConfiguracionReglaRest {
    private final ConfiguracionReglaService configuracionReglaService;

    @PostMapping("/list-page-configuracion-regla")
    public ResponseEntity<Page<ConfiguracionReglaResponseDto>> listPageConfiguracionRegla(@RequestBody ConfiguracionReglaSearchDto searchDto) {
        return ResponseEntity.ok(configuracionReglaService.search(searchDto));
    }

    @PostMapping("/create-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> create(@Valid @RequestBody ConfiguracionReglaRequestDto dto) {
        return ResponseEntity.ok(configuracionReglaService.create(dto));
    }

    @PostMapping("/update-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> update(@Valid @RequestBody ConfiguracionReglaRequestDto dto) {
        return ResponseEntity.ok(configuracionReglaService.update(dto));
    }

    @PostMapping("/delete-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> delete(@RequestBody ConfiguracionReglaRequestDto dto) {
        return ResponseEntity.ok(configuracionReglaService.delete(dto));
    }

    @PostMapping("/get-configuracion-regla")
    public ResponseEntity<ConfiguracionReglaResponseDto> getById(@RequestBody ConfiguracionReglaRequestDto dto) {
        return ResponseEntity.ok(configuracionReglaService.get(dto.getCodigo()));
    }
}