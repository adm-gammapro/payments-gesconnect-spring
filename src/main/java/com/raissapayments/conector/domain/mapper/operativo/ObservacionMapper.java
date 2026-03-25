package com.raissapayments.conector.domain.mapper.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.ObservacionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ObservacionResponseDto;
import com.raissapayments.conector.domain.entity.operativo.ObservacionEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class,
        SolicitudMapper.class,
})
public abstract class ObservacionMapper extends EntityMapper<ObservacionEntity, Long> {
    protected ObservacionMapper() { super(ObservacionEntity.class); }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "solicitud", source = "solicitudId")
    @Mapping(target = "estadoRegistro", constant = Constante.ESTADO_ACTIVO)
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract ObservacionEntity requestDtoToEntity(ObservacionRequestDto dto);

    @Mapping(target = "solicitudId", source = "solicitud.id")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract ObservacionResponseDto entityToResponseDto(ObservacionEntity entity);
}