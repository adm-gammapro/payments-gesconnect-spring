package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.response.ConfiguracionReglaResponseDto;
import com.raissapayments.conector.service.operativo.ConfiguracionReglaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracion-regla")
@RequiredArgsConstructor
public class ConfiguracionReglaRest {
    private final ConfiguracionReglaService configuracionReglaService;

    @PostMapping("/list-page-configuracion-regla")
    public ResponseEntity<Page<ConfiguracionReglaResponseDto>> listPageConfiguracionRegla(@RequestParam(required = false) Long codigoRegla,
                                                                                          @RequestParam(required = false) Long codigoCategoria,
                                                                                          @RequestParam(required = false) String codigoModo,
                                                                                          @RequestParam(required = false) String estadoRegistro,
                                                                                          @RequestParam(defaultValue = "0") int page,
                                                                                          @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        return ResponseEntity.ok(
                configuracionReglaService.search(codigoRegla, codigoCategoria, codigoModo, estadoRegistro, pageable)
        );
    }
}