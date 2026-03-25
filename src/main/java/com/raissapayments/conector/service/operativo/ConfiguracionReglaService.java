package com.raissapayments.conector.service.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ConfiguracionReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ConfiguracionReglaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConfiguracionReglaService {
    ConfiguracionReglaResponseDto create(ConfiguracionReglaRequestDto dto);
    ConfiguracionReglaResponseDto update(ConfiguracionReglaRequestDto dto);
    ConfiguracionReglaResponseDto delete(ConfiguracionReglaRequestDto dto);
    ConfiguracionReglaResponseDto get(Long codigo);

    Page<ConfiguracionReglaResponseDto> search(Long codigoRegla,
                                               Long codigoCategoria,
                                               String codigoModo,
                                               String estadoRegistro,
                                               Pageable pageable);
}