package com.raissapayments.conector.domain.mapper.operativo;

import com.raissapayments.conector.domain.dto.operativo.response.SolicitudResponseDto;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class,
        CargoSolicitudMapper.class
})
public abstract class SolicitudMapper extends EntityMapper<SolicitudEntity, Long> {
    protected SolicitudMapper() { super(SolicitudEntity.class); }

    @Mapping(target = "estadoSolicitud", source = "estadoSolicitud.codigo")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "cargos", source = "cargos")
    public abstract SolicitudResponseDto entityToResponseDto(SolicitudEntity entity);
}