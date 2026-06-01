package com.raissapayments.conector.domain.mapper.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.CabeceraEjecucionResponseDto;
import com.raissapayments.conector.domain.entity.operativo.CabeceraEjecucionEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MapperUtil.class})
public abstract class CabeceraEjecucionMapper extends EntityMapper<CabeceraEjecucionEntity, Long> {
    protected CabeceraEjecucionMapper() {
        super(CabeceraEjecucionEntity.class);
    }

    @Mapping(target = "estadoRegistro", source = "estadoRegistro",
            qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns",
            qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "fechaInicioProceso", source = "fechaInicioProceso",
            qualifiedByName = "mapLocalDateTimeToStringDateTime")
    @Mapping(target = "fechaFinProceso", source = "fechaFinProceso",
            qualifiedByName = "mapLocalDateTimeToStringDateTime")
    public abstract CabeceraEjecucionResponseDto entityToResponseDto(CabeceraEjecucionEntity entity);

    // Registro: mapea campos de auditoría INS desde el request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoRegistro", constant = "S")
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract CabeceraEjecucionEntity requestDtoToEntity(CabeceraEjecucionRequestDto dto);

    // Actualización: aplica sobre la entidad existente y setea campos MOD
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoJob", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "fechaInicioProceso", ignore = true)
    @Mapping(target = "codigoSistema", ignore = true)
    @Mapping(target = "codigoCliente", ignore = true)
    @Mapping(target = "audiFechIns", ignore = true)
    @Mapping(target = "audiUsuario", ignore = true)
    @Mapping(target = "audiNomTerminal", ignore = true)
    @Mapping(target = "audiIp", ignore = true)
    @Mapping(target = "audiFechaMod", source = "fechaAuditoria")
    @Mapping(target = "audiUsuMod", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminalMod", source = "terminalAuditoria")
    @Mapping(target = "audiIpMod", source = "ipAuditoria")
    public abstract void updateEntityFromDto(CabeceraEjecucionRequestDto dto,
                                             @MappingTarget CabeceraEjecucionEntity entity);
}