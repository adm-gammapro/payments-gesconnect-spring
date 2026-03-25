package com.raissapayments.conector.domain.mapper.commons;

import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Clase base para los mappers de entidades
 *
 * @param <T> Tipo de la entidad
 * @param <ID> Tipo del id de la entidad
 * @since 1.0.0
 */
@SuppressWarnings("java:S119")
public abstract class EntityMapper<T, ID> {
    @Setter(value = AccessLevel.PRIVATE, onMethod_ = @Autowired)
    private EntityManager entityManager;

    private final Class<T> entityClass;

    public EntityMapper(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Crea una {@link T} con el id indicado
     *
     * @param id Id de la entidad
     * @return {@link T} con el id indicado
     */
    public T idToEntity(ID id) {
        return id != null ? entityManager.getReference(this.entityClass, id) : null;
    }
}
