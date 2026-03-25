package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ObservacionResponseDto;
import com.raissapayments.conector.service.operativo.ObservacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observaciones")
@RequiredArgsConstructor
public class ObservacionRest {
    private final ObservacionService observacionService;

    @PostMapping
    public ResponseEntity<ObservacionResponseDto> crear(@Valid @RequestBody ObservacionRequestDto request) {
        return ResponseEntity.ok(observacionService.crearObservacion(request));
    }
}