package com.raissapayments.conector.domain.mapper.administrativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.administrativo.request.TipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.TipoPagoResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.TipoPagoEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        uses = {
        MapperUtil.class
})
public abstract class TipoPagoMapper extends EntityMapper<TipoPagoEntity, Long> {
    protected TipoPagoMapper() { super(TipoPagoEntity.class); }

    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract TipoPagoResponseDto entityToResponseDto(TipoPagoEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoRegistro", constant = Constante.ESTADO_ACTIVO)
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract TipoPagoEntity requestDtoToEntity(TipoPagoRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract void update(@MappingTarget TipoPagoEntity entity, TipoPagoRequestDto dto);
}