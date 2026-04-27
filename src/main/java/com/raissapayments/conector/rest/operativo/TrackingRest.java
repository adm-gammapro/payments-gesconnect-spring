package com.raissapayments.conector.rest.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.TrackingRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.TrackingResponseDto;
import com.raissapayments.conector.service.operativo.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tracking")
@RequiredArgsConstructor
public class TrackingRest {
    private final TrackingService trackingService;

    @PostMapping("/list-tracking")
    public ResponseEntity<List<TrackingResponseDto>> listarCuentasPage(@RequestBody(required = false) TrackingRequestDto trackingRequest) {
        return ResponseEntity.ok(trackingService.listTracking(trackingRequest.getIdSolicitud()));
    }
}
