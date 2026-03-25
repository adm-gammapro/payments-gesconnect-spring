package com.raissapayments.conector.domain.mapper.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.HistorialAutorizacionRequestDto;
import com.raissapayments.conector.domain.entity.operativo.HistorialAutorizacionEntity;
import com.raissapayments.conector.domain.mapper.administrativo.CategoriaMapper;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
                CategoriaMapper.class,
                SolicitudMapper.class,
                MapperUtil.class
        })
public abstract class HistorialAutorizacionMapper extends EntityMapper<HistorialAutorizacionEntity, Long> {
    protected HistorialAutorizacionMapper() { super(HistorialAutorizacionEntity.class); }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "solicitud", source = "solicitudId")
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
    public abstract HistorialAutorizacionEntity requestDtoToEntity(HistorialAutorizacionRequestDto dto);
}