package com.raissapayments.conector.domain.mapper.operativo;

import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.operativo.request.UsuarioAutorizadorTipoPagoRequestDto;
import com.raissapayments.conector.domain.dto.operativo.response.UsuarioAutorizadorTipoPagoResponseDto;
import com.raissapayments.conector.domain.entity.operativo.UsuarioAutorizadorTipoPagoEntity;
import com.raissapayments.conector.domain.mapper.administrativo.TipoPagoMapper;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class,
        TipoPagoMapper.class
})
public abstract class UsuarioAutorizadorTipoPagoMapper extends EntityMapper<UsuarioAutorizadorTipoPagoEntity, Long> {
    protected UsuarioAutorizadorTipoPagoMapper() { super(UsuarioAutorizadorTipoPagoEntity.class); }

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
    public abstract UsuarioAutorizadorTipoPagoEntity requestDtoToEntity(UsuarioAutorizadorTipoPagoRequestDto dto);

    @Mapping(target = "tipoPagoId", source = "tipoPago.id")
    @Mapping(target = "tipoPagoNombre", source = "tipoPago.descripcion")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract UsuarioAutorizadorTipoPagoResponseDto entityToResponseDto(UsuarioAutorizadorTipoPagoEntity entity);
}