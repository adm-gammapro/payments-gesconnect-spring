package com.raissapayments.conector.domain.mapper.operativo;

import com.raissapayments.conector.domain.dto.operativo.response.TrackingResponseDto;
import com.raissapayments.conector.domain.entity.operativo.TrackingEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MapperUtil.class,
})
public abstract class TrackingMapper extends EntityMapper<TrackingEntity, Long> {
    protected TrackingMapper() { super(TrackingEntity.class); }

    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "fecha", source = "fechaCarga", qualifiedByName = "mapLocalDateTimeToStringDateTime")
    public abstract TrackingResponseDto entityToResponseDto(TrackingEntity entity);
}
