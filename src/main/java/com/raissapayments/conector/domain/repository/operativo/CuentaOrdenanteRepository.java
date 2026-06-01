package com.raissapayments.conector.domain.repository.operativo;

import com.raissapayments.conector.domain.entity.operativo.CuentaOrdenanteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaOrdenanteRepository extends JpaRepository<CuentaOrdenanteEntity, Long> {
    CuentaOrdenanteEntity findByNumeroCuentaOrdenanteAndEstadoRegistro(String numeroCuentaOrdenante, String estadoRegistro);

    Page<CuentaOrdenanteEntity> findByNumeroCuentaOrdenanteContainingIgnoreCaseAndEstadoRegistro(String numeroCuentaOrdenante,
                                                                                                 String estadoRegistro,
                                                                                                 Pageable pageable);

    List<CuentaOrdenanteEntity> findByEstadoRegistro(String estadoRegistro);
}