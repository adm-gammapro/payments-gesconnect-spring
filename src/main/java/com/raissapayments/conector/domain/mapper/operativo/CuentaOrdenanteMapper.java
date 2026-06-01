package com.raissapayments.conector.domain.mapper.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.CuentaOrdenanteRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.CuentaOrdenanteResponseDto;
import com.raissapayments.conector.domain.entity.operativo.CuentaOrdenanteEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
        uses = {MapperUtil.class})
public abstract class CuentaOrdenanteMapper extends EntityMapper<CuentaOrdenanteEntity, Long> {
    protected CuentaOrdenanteMapper() {
        super(CuentaOrdenanteEntity.class);
    }

    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "codigo", source = "codigo")
    public abstract CuentaOrdenanteResponseDto entityToResponseDto(CuentaOrdenanteEntity entity);

    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "estadoRegistro", constant = Constante.ESTADO_ACTIVO)
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract CuentaOrdenanteEntity requestDtoToEntity(CuentaOrdenanteRequestDto dto);

    @Mapping(target = "codigo", ignore = true)
    @Mapping(target = "estadoRegistro", ignore = true)
    @Mapping(target = "audiFechIns", ignore = true)
    @Mapping(target = "audiUsuario", ignore = true)
    @Mapping(target = "audiNomTerminal", ignore = true)
    @Mapping(target = "audiIp", ignore = true)
    @Mapping(target = "passwordOrdenante", source ="passwordOrdenante")
    @Mapping(target = "audiFechaMod", source = "fechaAuditoria")
    @Mapping(target = "audiUsuMod", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminalMod", source = "terminalAuditoria")
    @Mapping(target = "audiIpMod", source = "ipAuditoria")
    public abstract void update(@MappingTarget CuentaOrdenanteEntity entity, CuentaOrdenanteRequestDto dto);
}
