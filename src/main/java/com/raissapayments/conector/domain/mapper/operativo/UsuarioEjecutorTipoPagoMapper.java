package com.raissapayments.conector.domain.mapper.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioEjecutorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioEjecutorTipoPagoResponseDto;
import com.raissapayments.conector.domain.entity.operativo.UsuarioEjecutorTipoPagoEntity;
import com.raissapayments.conector.domain.mapper.administrativo.TipoPagoMapper;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class,
        TipoPagoMapper.class
})
public abstract class UsuarioEjecutorTipoPagoMapper extends EntityMapper<UsuarioEjecutorTipoPagoEntity, Long> {
    protected UsuarioEjecutorTipoPagoMapper() { super(UsuarioEjecutorTipoPagoEntity.class); }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoPago", source = "tipoPagoId")
    @Mapping(target = "estadoRegistro", constant = Constante.ESTADO_ACTIVO)
    @Mapping(target = "audiFechIns", source = "fechaAuditoria")
    @Mapping(target = "audiUsuario", source = "usuarioAuditoria")
    @Mapping(target = "audiNomTerminal", source = "terminalAuditoria")
    @Mapping(target = "audiIp", source = "ipAuditoria")
    @Mapping(target = "audiFechaMod", ignore = true)
    @Mapping(target = "audiUsuMod", ignore = true)
    @Mapping(target = "audiNomTerminalMod", ignore = true)
    @Mapping(target = "audiIpMod", ignore = true)
    public abstract UsuarioEjecutorTipoPagoEntity requestDtoToEntity(UsuarioEjecutorTipoPagoRequestDto dto);

    @Mapping(target = "tipoPagoId", source = "tipoPago.id")
    @Mapping(target = "tipoPagoNombre", source = "tipoPago.descripcion")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract UsuarioEjecutorTipoPagoResponseDto entityToResponseDto(UsuarioEjecutorTipoPagoEntity entity);
}