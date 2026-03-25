package com.raissapayments.conector.domain.mapper.operativo;

import com.raissapayments.conector.domain.dto.operativo.response.CargoSolicitudResponseDto;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
            MapperUtil.class,
            AbonoSolicitudMapper.class
        })
public abstract class CargoSolicitudMapper extends EntityMapper<CargoSolicitudEntity, Long> {
    protected CargoSolicitudMapper() { super(CargoSolicitudEntity.class); }

    @Mapping(target = "solicitudId", source = "solicitud.id")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "abonos", source = "abonos")
    public abstract CargoSolicitudResponseDto entityToResponseDto(CargoSolicitudEntity entity);
}