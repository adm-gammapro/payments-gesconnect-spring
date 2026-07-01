package com.raissapayments.conector.domain.repository.commons;

import com.raissapayments.conector.domain.entity.commons.ConstanteSistemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConstanteSistemaRepository extends JpaRepository<ConstanteSistemaEntity, String> {
    Optional<ConstanteSistemaEntity> findByCodigoAndEstadoRegistro(String codigo,
                                                                   String estadoRegistro);
}