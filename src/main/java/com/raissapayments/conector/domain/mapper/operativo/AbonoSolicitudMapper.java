package com.raissapayments.conector.domain.mapper.operativo;

import com.raissapayments.conector.domain.dto.operativo.response.AbonoSolicitudResponseDto;
import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
            MapperUtil.class
        })
public abstract class AbonoSolicitudMapper extends EntityMapper<AbonosSolicitudEntity, Long> {
    protected AbonoSolicitudMapper() { super(AbonosSolicitudEntity.class); }

    @Mapping(target = "cargoSolicitudId", source = "cargoSolicitud.id")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    public abstract AbonoSolicitudResponseDto entityToResponseDto(AbonosSolicitudEntity entity);
}