package com.raissapayments.conector.domain.mapper.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioCategoriaRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioCategoriaResponseDto;
import com.raissapayments.conector.domain.entity.operativo.UsuarioCategoriaEntity;
import com.raissapayments.conector.domain.mapper.administrativo.CategoriaMapper;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class,
        CategoriaMapper.class
})
public abstract class UsuarioCategoriaMapper extends EntityMapper<UsuarioCategoriaEntity, Long> {
    protected UsuarioCategoriaMapper() { super(UsuarioCategoriaEntity.class); }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", source = "categoriaId")
    @Mapping(target = "estadoRegistro", constant = Constante.ESTADO_ACTIVO)
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract UsuarioCategoriaEntity requestDtoToEntity(UsuarioCategoriaRequestDto dto);

    @Mapping(target = "categoriaId", source = "categoria.id")
    @Mapping(target = "categoriaNombre", source = "categoria.descripcion")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract UsuarioCategoriaResponseDto entityToResponseDto(UsuarioCategoriaEntity entity);
}