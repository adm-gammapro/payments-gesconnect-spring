package com.raissapayments.conector.domain.mapper.commons;

import com.raissapayments.conector.domain.dto.commons.EstadoSolicitudDto;
import com.raissapayments.conector.domain.entity.commons.EstadoSolicitudEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                MapperUtil.class
        })
public abstract class EstadoSolicitudMapper extends EntityMapper<EstadoSolicitudEntity, String> {
        protected EstadoSolicitudMapper() { super(EstadoSolicitudEntity.class); }

        public abstract EstadoSolicitudDto entityToResponseDto(EstadoSolicitudEntity entity);
}
