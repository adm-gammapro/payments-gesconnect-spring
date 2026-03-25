package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.CargaSolicitudJsonRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.CargaSolicitudRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.CargaSolicitudResponseDto;
import com.raissapayments.conector.service.operativo.CargaSolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/solicitudes")
@RequiredArgsConstructor
public class CargaSolicitudRest {
    private final CargaSolicitudService service;

    @PostMapping(value = "/cargar-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CargaSolicitudResponseDto> cargarJson(@RequestBody CargaSolicitudJsonRequestDto req
    ) {
        return ResponseEntity.ok(service.cargarDesdeJson(req));
    }
}