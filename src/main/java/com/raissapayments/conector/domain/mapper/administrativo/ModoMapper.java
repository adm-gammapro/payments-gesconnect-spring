package com.raissapayments.conector.domain.mapper.administrativo;

import com.raissapayments.conector.domain.dto.administrativo.response.ModoResponseDto;
import com.raissapayments.conector.domain.entity.administrativo.ModoEntity;
import com.raissapayments.conector.domain.mapper.commons.EntityMapper;
import com.raissapayments.conector.domain.mapper.commons.MapperUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para mapear la entidad {@link ModoEntity} a sus correspondientes DTOs y viceversa
 *
 * @since 1.0.0
 */
@Mapper(
        componentModel = "spring",
        uses = {
                MapperUtil.class
        }
)
public abstract class ModoMapper  extends EntityMapper<ModoEntity, Long> {
    protected ModoMapper() {
        super(ModoEntity.class);
    }

    /**
     * Carga los datos de un entity en un dto
     *
     * @param entity datos del entity
     * @return {@link ModoResponseDto}
     */
    @Mapping(target = "audiFechIns", source = "audiFechIns", qualifiedByName = "mapLocalDateTimeToString")
    @Mapping(target = "estadoRegistro", source = "estadoRegistro", qualifiedByName = "mapStringToEstadoRegistroEnum")
    public abstract ModoResponseDto entityToResponseDto(ModoEntity entity);
}
