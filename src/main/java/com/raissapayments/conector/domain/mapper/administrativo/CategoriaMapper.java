package com.raissapayments.conector.domain.mapper.administrativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.administrativo.request.CategoriaRequestDto;
import com.raissapayments.conector.domain.dto.administrativo.response.CategoriaResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.CategoriaEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class
})
public abstract class CategoriaMapper extends EntityMapper<CategoriaEntity, Long> {
    protected CategoriaMapper() { super(CategoriaEntity.class); }

    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "codigo", source = "id")
    public abstract CategoriaResponseDto entityToResponseDto(CategoriaEntity entity);

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
    public abstract CategoriaEntity requestDtoToEntity(CategoriaRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "audiFechIns", ignore = true)
    @Mapping(target = "audiUsuario", ignore = true)
    @Mapping(target = "audiNomTerminal", ignore = true)
    @Mapping(target = "audiIp", ignore = true)
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract void update(@MappingTarget CategoriaEntity entity, CategoriaRequestDto dto);
}