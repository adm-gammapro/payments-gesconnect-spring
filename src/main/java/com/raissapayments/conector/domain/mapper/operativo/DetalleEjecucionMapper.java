package com.raissapayments.conector.domain.mapper.operativo;

import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.DetalleEjecucionResponseDto;
import com.raissapayments.conector.domain.entity.operativo.DetalleEjecucionEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MapperUtil.class})
public abstract class DetalleEjecucionMapper extends EntityMapper<DetalleEjecucionEntity, Long> {
    protected DetalleEjecucionMapper() {
        super(DetalleEjecucionEntity.class);
    }

    @Mapping(target = "estadoRegistro", source = "estadoRegistro",
            qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns",
            qualifiedByName = "mapLocalDateTimeToString")
    public abstract DetalleEjecucionResponseDto entityToResponseDto(DetalleEjecucionEntity entity);

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
    public abstract DetalleEjecucionEntity requestDtoToEntity(DetalleEjecucionRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "audiFechIns", ignore = true)
    @Mapping(target = "audiUsuario", ignore = true)
    @Mapping(target = "audiNomTerminal", ignore = true)
    @Mapping(target = "audiIp", ignore = true)
    @Mapping(target = "audiFechaMod", source = "fechaAuditoria")
    @Mapping(target = "audiUsuMod", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminalMod", source = "terminalAuditoria")
    @Mapping(target = "audiIpMod", source = "ipAuditoria")
    public abstract void updateEntityFromDto(DetalleEjecucionRequestDto dto,
                                             @MappingTarget DetalleEjecucionEntity entity);
}