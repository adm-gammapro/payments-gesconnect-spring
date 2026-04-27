package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.CuentaOrdenanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaOrdenanteRepository extends JpaRepository<CuentaOrdenanteEntity, Long> {
    CuentaOrdenanteEntity findByNumeroCuentaOrdenanteAndEstadoRegistro(String numeroCuentaOrdenante, String estadoRegistro);
}
