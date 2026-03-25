package com.raissapayments.conector.domain.mapper.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.ConfiguracionReglaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.ConfiguracionReglaResponseDto;
import com.raissapayments.conector.domain.entity.operativo.ConfiguracionReglaEntity;
import com.raissapayments.conector.domain.mapper.administrativo.CategoriaMapper;
import com.raissapayments.conector.domain.mapper.administrativo.ModoMapper;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class,
        ModoMapper.class,
        CategoriaMapper.class,
        ReglaMapper.class,
})
public abstract class ConfiguracionReglaMapper extends EntityMapper<ConfiguracionReglaEntity, Long> {
    protected ConfiguracionReglaMapper() { super(ConfiguracionReglaEntity.class); }

    @Mapping(target = "codigo", source = "id")
    @Mapping(target = "codigoRegla", source = "regla.id")
    @Mapping(target = "codigoCategoria", source = "categoria.id")
    @Mapping(target = "codigoModo", source = "modo.codigo")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract ConfiguracionReglaResponseDto entityToResponseDto(ConfiguracionReglaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", source = "codigoCategoria")
    @Mapping(target = "regla", source = "codigoRegla")
    @Mapping(target = "modo", source = "codigoModo")
    @Mapping(target = "estadoRegistro", constant = Constante.ESTADO_ACTIVO)
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract ConfiguracionReglaEntity requestDtoToEntity(ConfiguracionReglaRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", source = "codigoCategoria")
    @Mapping(target = "regla", source = "codigoRegla")
    @Mapping(target = "modo", source = "codigoModo")
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "audiFechIns", ignore = true)
    @Mapping(target = "audiUsuario", ignore = true)
    @Mapping(target = "audiNomTerminal", ignore = true)
    @Mapping(target = "audiIp", ignore = true)
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract void update(@MappingTarget ConfiguracionReglaEntity entity, ConfiguracionReglaRequestDto dto);
}