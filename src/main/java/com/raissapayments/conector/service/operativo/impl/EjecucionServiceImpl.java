package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.general.dto.ResponseDTO;
import com.raissa.comun.util.Constante;
import com.raissa.comun.util.ConstanteError;
import com.raissapayments.conector.config.encrypted.AESUtil;
import com.raissapayments.conector.domain.dto.commons.InstitucionFinancieraDto;
import com.raissapayments.conector.domain.dto.operativo.request.LoginAlfinRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.CabeceraEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.ConfirmaTransRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.ConsultaTransRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.DetalleEjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.EjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConfirmaCredencialesDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConfirmaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConsultaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConsultasCredencialesDto;
import com.raissapayments.conector.domain.dto.operativo.response.AccountsResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.LoginResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.SaldoResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.CabeceraEjecucionResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.ConfirmaTransGetResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.ConsultaTransGetResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.EjecucionResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.GroupConfirmaTransResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.GroupConsultaTransResponseDto;
import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CuentaOrdenanteEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.repository.operativo.AbonosSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.CargoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.CuentaOrdenanteRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.exception.operativo.ConnectionException;
import com.raissapayments.conector.exception.operativo.ErrorControladoException;
import com.raissapayments.conector.service.operativo.ApiService;
import com.raissapayments.conector.service.operativo.CabeceraEjecucionService;
import com.raissapayments.conector.service.operativo.DetalleEjecucionService;
import com.raissapayments.conector.service.operativo.EjecucionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EjecucionServiceImpl implements EjecucionService {
    @Value("${raissa.bff.apikey}") String apiKey;

    private final SolicitudRepository solicitudRepository;
    private final CargoSolicitudRepository cargoSolicitudRepository;
    private final AbonosSolicitudRepository abonosSolicitudRepository;
    private final CuentaOrdenanteRepository cuentaOrdenanteRepository;
    private final CabeceraEjecucionService cabeceraEjecucionService;
    private final DetalleEjecucionService detalleEjecucionService;
    private final ApiService apiService;

    //Utilitarios
    private final AESUtil aesUtil;

    public EjecucionResponseDto consultarTransferenciaInmediata(EjecucionRequestDto request) throws Exception {
        List<ConsultaTransRequestDto> listConsulta;
        EjecucionResponseDto responseDto = new EjecucionResponseDto();

        Optional<SolicitudEntity> opt = solicitudRepository.findById(request.getIdSolicitud());
        if (opt.isEmpty()) {
            responseDto.setStatus(Constante.KEY_ERROR_CODE);
            responseDto.setMessage("El solicitud no existe");
            return responseDto;
        }

        SolicitudEntity solicitud = opt.get();
        List<CargoSolicitudEntity> listCargo = cargoSolicitudRepository.findBySolicitudIdAndEstadoRegistro(solicitud.getId(),
                Constante.ESTADO_ACTIVO);

        ConsultaTransRequestDto consulta;
        if (listCargo == null || listCargo.isEmpty()) {
            responseDto.setStatus(Constante.KEY_ERROR_CODE);
            responseDto.setMessage("No hay cargos que consultar");
            return responseDto;
        }

        List<GroupConsultasCredencialesDto> listIteraccion = new ArrayList<>();

        for (CargoSolicitudEntity cargo : listCargo) {
            listConsulta = new ArrayList<>();
            GroupConsultasCredencialesDto iteraccion = new GroupConsultasCredencialesDto();
            CuentaOrdenanteEntity cuentaOpt = cuentaOrdenanteRepository.findByNumeroCuentaOrdenanteAndEstadoRegistro(
                    cargo.getCuentaOrigen(),
                    Constante.ESTADO_ACTIVO
            );
            if (cuentaOpt == null) {
                continue;
            }

            List<AbonosSolicitudEntity> listAbonos = abonosSolicitudRepository.findAbonosPendientesConsultaByCargo(cargo.getId(),
                    Constante.ESTADO_ACTIVO);
            for (AbonosSolicitudEntity abono : listAbonos) {
                consulta = new ConsultaTransRequestDto();
                consulta.setIdSolicitud(request.getIdSolicitud());
                consulta.setIdCargoSolicitud(cargo.getId());
                consulta.setIdAbonoSolicitud(abono.getId());
                consulta.setClienteBaaS(aesUtil.decrypt(cuentaOpt.getUsuarioOrdenante()));
                consulta.setCuentaBaaS(cargo.getCuentaOrigen());

                String moneda;
                if (cargo.getMoneda().equals(Constante.CODIGO_MONEDA_SOLES_ISO)) {
                    moneda = Constante.CODIGO_MONEDA_SOLES;
                } else {
                    moneda = Constante.CODIGO_MONEDA_DOLARES;
                }
                consulta.setMoneda(moneda);

                consulta.setImporte(abono.getMontoDestino());
                consulta.setCodigoTransaccion(Constante.CODIGO_320);

                Optional<String> codigoSbs = Optional.ofNullable(request.getListInstituciones())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(i -> abono.getCodigoEntidadFinanciera().equals(i.getCodigo()))
                        .map(InstitucionFinancieraDto::getCodigoSbs)
                        .findFirst();
                consulta.setBancoDestino(codigoSbs.orElse(Constante.CODIGO_0));

                consulta.setSucursalDestino(Constante.CODIGO_0);
                consulta.setTarjeta(Constante.CONSTANTE_VACIA);
                consulta.setCciBeneficiario(abono.getCuentaDestino());
                consulta.setMismoTitular(abono.getMismotitular());
                consulta.setTipoDocumentoOrdenante(cuentaOpt.getTipoDocumentoOrdenante());
                consulta.setDocumentoOrdenante(cuentaOpt.getDocumentoOrdenante());
                consulta.setNombreOrdenante(cuentaOpt.getNombreOrdenante());
                if (cuentaOpt.getApellidoPaternoOrdenante() == null) {
                    consulta.setApellidoPaternoOrdenante(Constante.CONSTANTE_VACIA);
                } else {
                    consulta.setApellidoPaternoOrdenante(cuentaOpt.getApellidoPaternoOrdenante());
                }
                if (cuentaOpt.getApellidoMaternoOrdenante() == null) {
                    consulta.setApellidoMaternoOrdenante(Constante.CONSTANTE_VACIA);
                } else {
                    consulta.setApellidoMaternoOrdenante(cuentaOpt.getApellidoMaternoOrdenante());
                }

                listConsulta.add(consulta);
            }

            iteraccion.setListConsulta(listConsulta);

            LoginAlfinRequestDto loginDto = new LoginAlfinRequestDto();
            loginDto.setApiKey(apiKey);
            loginDto.setUsername(aesUtil.decrypt(cuentaOpt.getUsuarioOrdenante()));
            loginDto.setPassword(aesUtil.decrypt(cuentaOpt.getPasswordOrdenante()));
            loginDto.setProvider("alfin_api");
            loginDto.setCompany_code("");
            loginDto.setIndicadorValorAdicional(false);
            iteraccion.setLoginAlfin(loginDto);

            iteraccion.setNumeroCuentaCargo(cargo.getCuentaOrigen());
            iteraccion.setMontoCargo(cargo.getMontoCargo());

            listIteraccion.add(iteraccion);
        }
        responseDto = operarConsulta(listIteraccion,
                apiKey,
                request.getUsuarioAuditoria(),
                request.getFechaAuditoria(),
                request.getIdCabeceraEjecucion(),
                request.getIpAuditoria(),
                request.getTerminalAuditoria());

        return responseDto;
    }

    public EjecucionResponseDto confirmarTransferenciaInmediata(EjecucionRequestDto request) throws Exception {
        int registrosTotales;
        int registrosProcesados = 0;
        int registrosPendientes;
        int registrosErroneos = 0;
        List<ConfirmaTransRequestDto> listConfirmaciones = new ArrayList<>();
        EjecucionResponseDto responseDto = new EjecucionResponseDto();
        CabeceraEjecucionRequestDto cabeceraEjecucion = new CabeceraEjecucionRequestDto();
        CabeceraEjecucionResponseDto responseCabeceraEjecucion = new CabeceraEjecucionResponseDto();

        Optional<SolicitudEntity> opt = solicitudRepository.findById(request.getIdSolicitud());
        if (opt.isEmpty()) {
            responseDto.setStatus(Constante.KEY_ERROR_CODE);
            responseDto.setMessage("El solicitud no existe");
            return responseDto;
        }

        SolicitudEntity solicitud = opt.get();
        List<CargoSolicitudEntity> listCargo = cargoSolicitudRepository.findBySolicitudIdAndEstadoRegistro(solicitud.getId(),
                Constante.ESTADO_ACTIVO);

        ConfirmaTransRequestDto confirmacion;
        if (listCargo == null || listCargo.isEmpty()) {
            responseDto.setStatus(Constante.KEY_ERROR_CODE);
            responseDto.setMessage("No hay cargos que consultar");
            return responseDto;
        }

        List<GroupConfirmaCredencialesDto> listIteraccion = new ArrayList<>();

        for (CargoSolicitudEntity cargo : listCargo) {
            GroupConfirmaCredencialesDto iteraccion = new GroupConfirmaCredencialesDto();
            registrosTotales = cargo.getAbonos().size();
            registrosPendientes = cargo.getAbonos().size();

            cabeceraEjecucion.setCodigoJob(request.getIdSolicitud());
            cabeceraEjecucion.setCodigoCliente(request.getCodigoCliente());
            cabeceraEjecucion.setCodigoSistema(Constante.SISTEMA_PAYMENTS);
            cabeceraEjecucion.setFechaInicioProceso(LocalDateTime.now());
            cabeceraEjecucion.setRegistrosTotales(registrosTotales);
            cabeceraEjecucion.setRegistrosProcesados(registrosProcesados);
            cabeceraEjecucion.setRegistrosPendientes(registrosPendientes);
            cabeceraEjecucion.setRegistrosErroneos(registrosErroneos);
            cabeceraEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_PROCESANDO);
            cabeceraEjecucion.setProceso(Constante.PROCESO_CONFIRMACION);
            cabeceraEjecucion.setDetalleEjecucion("Iniciando confirmación");
            cabeceraEjecucion.setFechaAuditoria(LocalDateTime.now());
            cabeceraEjecucion.setUsuarioAuditoria(request.getUsuarioAuditoria());
            cabeceraEjecucion.setIpAuditoria(request.getIpAuditoria());
            cabeceraEjecucion.setTerminalAuditoria(request.getTerminalAuditoria());
            responseCabeceraEjecucion = cabeceraEjecucionService.registrar(cabeceraEjecucion);

            CuentaOrdenanteEntity cuentaOpt = cuentaOrdenanteRepository.findByNumeroCuentaOrdenanteAndEstadoRegistro(
                    cargo.getCuentaOrigen(),
                    Constante.ESTADO_ACTIVO
            );
            if (cuentaOpt == null) {
                continue;
            }

            List<AbonosSolicitudEntity> listAbonos = abonosSolicitudRepository.findAbonosPendientesConfirmacionByCargo(cargo.getId(),
                    Constante.ESTADO_ACTIVO);
            for (AbonosSolicitudEntity abono : listAbonos) {
                confirmacion = new ConfirmaTransRequestDto();
                confirmacion.setIdSolicitud(request.getIdSolicitud());
                confirmacion.setIdCargoSolicitud(cargo.getId());
                confirmacion.setIdAbonoSolicitud(abono.getId());
                confirmacion.setClienteBaaS(aesUtil.decrypt(cuentaOpt.getUsuarioOrdenante()));
                confirmacion.setCuentaBaaS(cargo.getCuentaOrigen());

                String moneda;
                if (cargo.getMoneda().equals(Constante.CODIGO_MONEDA_SOLES_ISO)) {
                    moneda = Constante.CODIGO_MONEDA_SOLES;
                } else {
                    moneda = Constante.CODIGO_MONEDA_DOLARES;
                }
                confirmacion.setMoneda(moneda);

                confirmacion.setImporte(abono.getMontoDestino());

                confirmacion.setTransferenciaId(abono.getTransferenciaId());
                confirmacion.setMpe001idl(abono.getMpe001idl());

                listConfirmaciones.add(confirmacion);
            }

            iteraccion.setListConfirmaciones(listConfirmaciones);

            LoginAlfinRequestDto loginDto = new LoginAlfinRequestDto();
            loginDto.setApiKey(apiKey);
            loginDto.setUsername(aesUtil.decrypt(cuentaOpt.getUsuarioOrdenante()));
            loginDto.setPassword(aesUtil.decrypt(cuentaOpt.getPasswordOrdenante()));
            loginDto.setProvider("alfin_api");
            loginDto.setCompany_code("");
            loginDto.setIndicadorValorAdicional(false);
            iteraccion.setLoginAlfin(loginDto);

            listIteraccion.add(iteraccion);
        }

        responseDto = operarConfirmar(listIteraccion,
                                      apiKey,
                                      request.getUsuarioAuditoria(),
                                      request.getFechaAuditoria(),
                                      responseCabeceraEjecucion.getId(),
                                      request.getIpAuditoria(),
                                      request.getTerminalAuditoria());

        return responseDto;
    }

    private EjecucionResponseDto operarConsulta(List<GroupConsultasCredencialesDto> iteracciones,
                                                String apiKey,
                                                String usuarioEjecucion,
                                                LocalDateTime fechaOperacion,
                                                Long idCabeceraEjecucion,
                                                String ipOperacion,
                                                String terminalOperacion) {
        EjecucionResponseDto ejecucionResponse = new EjecucionResponseDto();
        StringBuilder mensajeFinal = new StringBuilder();
        CabeceraEjecucionRequestDto cabeceraEjecucionRequest;
        CabeceraEjecucionResponseDto cabeceraEjecucionResponse = new CabeceraEjecucionResponseDto();
        int registrosTotales = 0;
        int registrosProcesados = 0;
        int registrosPendientes;
        int registrosErroneos = 0;

        try {
            cabeceraEjecucionResponse = cabeceraEjecucionService.buscarPorId(idCabeceraEjecucion);
            registrosTotales = cabeceraEjecucionResponse.getRegistrosTotales();
            registrosProcesados = cabeceraEjecucionResponse.getRegistrosProcesados();
            registrosErroneos = cabeceraEjecucionResponse.getRegistrosErroneos();
            for (GroupConsultasCredencialesDto iteraccion : iteracciones) {
                ResponseDTO<?> responseLoginDTO = apiService.login(iteraccion.getLoginAlfin());
                if(responseLoginDTO.getBody() == null) {
                    mensajeFinal.append("Ocurrieron problemas de autenticación");
                    continue;
                }

                if (responseLoginDTO.getBody().getClass() == LoginResponseDto.class) {
                    LoginResponseDto loginDTOResponse = (LoginResponseDto) responseLoginDTO.getBody();

                    GroupConsultaTransBffRequestDto consultas = new GroupConsultaTransBffRequestDto();
                    consultas.setListConsultaTransferencia(iteraccion.getListConsulta());

                    ResponseDTO<?> responseSaldoDTO = apiService.saldos(loginDTOResponse.getKey(),
                            apiKey,
                            iteraccion.getLoginAlfin().getUsername(),
                            iteraccion.getNumeroCuentaCargo());

                    if (responseSaldoDTO.getBody() == null) {
                        mensajeFinal.append("No se pudo recuperar saldo de cuenta: ")
                                .append(iteraccion.getNumeroCuentaCargo());
                        continue;
                    }
                    if (responseSaldoDTO.getBody().getClass() == SaldoResponseDto.class) {
                        SaldoResponseDto saldoDTOResponse = (SaldoResponseDto) responseSaldoDTO.getBody();
                        if (saldoDTOResponse.getAccounts() != null) {
                            int indicadorErrorSaldo = 0;
                            for (AccountsResponseDto account : saldoDTOResponse.getAccounts()) {
                                if (account.getNumber().equals(iteraccion.getNumeroCuentaCargo()) &&
                                        iteraccion.getMontoCargo().compareTo(BigDecimal.valueOf(account.getBalance())) > 0) {
                                        mensajeFinal.append("Saldo insuficiente en cuenta: ")
                                                .append(iteraccion.getNumeroCuentaCargo())
                                                .append(Constante.SEPARADOR_ERRORES);
                                        indicadorErrorSaldo++;
                                }
                            }
                            if(indicadorErrorSaldo > 0) {
                                mensajeFinal.append("Cuenta de cargo con saldo insuficiente: ")
                                        .append(iteraccion.getNumeroCuentaCargo());
                                continue;
                            }
                        }
                    }

                    ResponseDTO<?> responseConsultas = apiService.consultaTranferencia(loginDTOResponse.getKey(), apiKey, consultas);

                    if(responseConsultas.getBody() == null) {
                        mensajeFinal.append("No hubo respuesta en consulta de transferencias")
                                .append(iteraccion.getNumeroCuentaCargo());
                        continue;
                    }

                    if (responseConsultas.getBody().getClass() == GroupConsultaTransResponseDto.class) {
                        GroupConsultaTransResponseDto response = (GroupConsultaTransResponseDto) responseConsultas.getBody();
                        if (response.getStatus().equals(Constante.KEY_SUCCESS_CODE)) {
                            Map<Long, ConsultaTransRequestDto> mapaConsulta = iteraccion.getListConsulta().stream()
                                    .collect(Collectors.toMap(
                                            ConsultaTransRequestDto::getIdAbonoSolicitud,
                                            Function.identity()
                                    ));

                            for (ConsultaTransGetResponseDto responseConsultaindividual : response.getListRespuestaConsultaTransferencia()) {
                                ConsultaTransRequestDto request = mapaConsulta.get(responseConsultaindividual.getIdAbonoSolicitud());

                                Optional<AbonosSolicitudEntity> abonoOpt = abonosSolicitudRepository.findById(request.getIdAbonoSolicitud());
                                String moneda;
                                String descripcionMoneda;
                                String codigoEntidadFinanciera;

                                if (abonoOpt.isPresent()) {
                                    AbonosSolicitudEntity abono = abonoOpt.get();
                                    moneda = abono.getMoneda();
                                    descripcionMoneda = "PEN".equals(moneda) ? "Soles" : "Dólares";
                                    codigoEntidadFinanciera = abono.getCodigoEntidadFinanciera();
                                } else {
                                    throw new ErrorControladoException("Abono no encontrado con ID: " + request.getIdAbonoSolicitud());
                                }

                                if (responseConsultaindividual.getStatus().equals(Constante.KEY_SUCCESS_CODE)) {
                                    registrosProcesados++;
                                    registrosPendientes = registrosTotales - (registrosErroneos + registrosProcesados);

                                    cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                                    cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                                    cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                                    cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                                    cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                                    cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                                    cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_PROCESANDO);
                                    cabeceraEjecucionRequest.setProceso(Constante.PROCESO_VALIDACION);
                                    cabeceraEjecucionRequest.setDetalleEjecucion(ConstanteError.MENSAJE_PROCESANDO);
                                    cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                                    cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                                    cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                                    cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                                    cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                                    DetalleEjecucionRequestDto detalleEjecucion = new DetalleEjecucionRequestDto();
                                    detalleEjecucion.setCodigoCabeceraEjecucion(cabeceraEjecucionResponse.getId());
                                    detalleEjecucion.setNumeroCuenta(request.getCciBeneficiario());
                                    detalleEjecucion.setMonedaCuenta(moneda);
                                    detalleEjecucion.setDescripcionMoneda(descripcionMoneda);
                                    detalleEjecucion.setCodigoEntidadFinanciera(codigoEntidadFinanciera);
                                    detalleEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_DET_PROCESADO);
                                    detalleEjecucion.setDetalleEjecucion(ConstanteError.MENSAJE_VALIDACION_EXITOSA);
                                    detalleEjecucion.setFechaAuditoria(LocalDateTime.now());
                                    detalleEjecucion.setUsuarioAuditoria(usuarioEjecucion);
                                    detalleEjecucion.setIpAuditoria(ipOperacion);
                                    detalleEjecucion.setTerminalAuditoria(terminalOperacion);
                                    detalleEjecucionService.registrar(detalleEjecucion);
                                } else {
                                    String mensaje = String.format(
                                            "Observacion en la operacion de la cuenta %s con importe %s: %s",
                                            request.getCciBeneficiario(),
                                            request.getImporte(),
                                            responseConsultaindividual.getDscRespuesta()
                                    );

                                    mensajeFinal.append(mensaje).append(Constante.SEPARADOR_ERRORES);

                                    registrosErroneos++;
                                    registrosPendientes = registrosTotales - (registrosErroneos + registrosProcesados);

                                    cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                                    cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                                    cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                                    cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                                    cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                                    cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                                    cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_PROCESANDO);
                                    cabeceraEjecucionRequest.setProceso(Constante.PROCESO_VALIDACION);
                                    cabeceraEjecucionRequest.setDetalleEjecucion(ConstanteError.MENSAJE_PROCESANDO);
                                    cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                                    cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                                    cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                                    cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                                    cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                                    DetalleEjecucionRequestDto detalleEjecucion = new DetalleEjecucionRequestDto();
                                    detalleEjecucion.setCodigoCabeceraEjecucion(cabeceraEjecucionResponse.getId());
                                    detalleEjecucion.setNumeroCuenta(request.getCciBeneficiario());
                                    detalleEjecucion.setMonedaCuenta(moneda);
                                    detalleEjecucion.setDescripcionMoneda(descripcionMoneda);
                                    detalleEjecucion.setCodigoEntidadFinanciera(codigoEntidadFinanciera);
                                    detalleEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_DET_ERRONEO);
                                    String truncado;
                                    if(responseConsultaindividual.getEstado().equals(Constante.ESTADO_ALFIN_CONF_ERROR)){
                                        truncado = "Error de configuración para acceso a la cuenta de cargo";
                                    } else if(responseConsultaindividual.getEstado().equals(Constante.ESTADO_ALFIN_SEG_ERROR)){
                                        truncado = "Error de seguridad";
                                    } else if(responseConsultaindividual.getEstado().equals(Constante.ESTADO_ALFIN_PLAT_ERROR)){
                                        truncado = "Error en la plataforma del banco";
                                    } else {
                                        truncado = mensaje.length() > 2000 ? mensaje.substring(0, 2000) : mensaje;
                                    }
                                    detalleEjecucion.setDetalleEjecucion(truncado);
                                    detalleEjecucion.setFechaAuditoria(LocalDateTime.now());
                                    detalleEjecucion.setUsuarioAuditoria(usuarioEjecucion);
                                    detalleEjecucion.setIpAuditoria(ipOperacion);
                                    detalleEjecucion.setTerminalAuditoria(terminalOperacion);
                                    detalleEjecucionService.registrar(detalleEjecucion);

                                }
                                actualizarDatosConsulta(responseConsultaindividual,
                                        usuarioEjecucion,
                                        fechaOperacion,
                                        ipOperacion,
                                        terminalOperacion);
                            }
                        } else {
                            mensajeFinal.append("Ocurrio un problema al realizar consulta de transferencias: ")
                                    .append(response.getMessage())
                                    .append(Constante.SEPARADOR_ERRORES);
                        }
                    }
                }
            }
            if (mensajeFinal.isEmpty()) {
                registrosPendientes = 0;

                cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO);
                cabeceraEjecucionRequest.setProceso(Constante.PROCESO_VALIDACION);
                cabeceraEjecucionRequest.setDetalleEjecucion("Validación procesada exitosamente");
                cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                ejecucionResponse.setStatus(Constante.KEY_SUCCESS_CODE);
            } else {
                registrosPendientes = 0;

                cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO_ERROR);
                cabeceraEjecucionRequest.setProceso(Constante.PROCESO_VALIDACION);
                String truncado = mensajeFinal.toString().length() > 2000 ? mensajeFinal.substring(0, 2000) : mensajeFinal.toString();
                cabeceraEjecucionRequest.setDetalleEjecucion(truncado);
                cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
                ejecucionResponse.setMessage(mensajeFinal.toString());
            }
        } catch (ConnectionException e){
            ejecucionResponse.setStatus(Constante.KEY_NOT_ACTION_CODE);
        } catch (Exception e) {
            registrosPendientes = 0;

            cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
            cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
            cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
            cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
            cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
            cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
            cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO_ERROR);
            cabeceraEjecucionRequest.setProceso(Constante.PROCESO_VALIDACION);
            cabeceraEjecucionRequest.setDetalleEjecucion("Error no controlado al realizar validación de transferencias");
            cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
            cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
            cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
            cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
            cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

            ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
            ejecucionResponse.setMessage("Error no controlado al realizar consulta de transferencias: " + e.getMessage());
        }

        return ejecucionResponse;
    }

    private EjecucionResponseDto operarConfirmar(List<GroupConfirmaCredencialesDto> iteracciones,
                                                 String apiKey,
                                                 String usuarioEjecucion,
                                                 LocalDateTime fechaOperacion,
                                                 Long idCabeceraEjecucion,
                                                 String ipOperacion,
                                                 String terminalOperacion) {
        EjecucionResponseDto ejecucionResponse = new EjecucionResponseDto();
        StringBuilder mensajeFinal = new StringBuilder();
        CabeceraEjecucionRequestDto cabeceraEjecucionRequest;
        CabeceraEjecucionResponseDto cabeceraEjecucionResponse = new CabeceraEjecucionResponseDto();
        int registrosTotales = 0;
        int registrosProcesados = 0;
        int registrosPendientes;
        int registrosErroneos = 0;

        try {
            cabeceraEjecucionResponse = cabeceraEjecucionService.buscarPorId(idCabeceraEjecucion);
            registrosTotales = cabeceraEjecucionResponse.getRegistrosTotales();
            registrosProcesados = cabeceraEjecucionResponse.getRegistrosProcesados();
            registrosErroneos = cabeceraEjecucionResponse.getRegistrosErroneos();

            for (GroupConfirmaCredencialesDto iteraccion : iteracciones) {
                ResponseDTO<?> responseLoginDTO = apiService.login(iteraccion.getLoginAlfin());

                if(responseLoginDTO.getBody() == null) {
                    mensajeFinal.append("Problemas de autenticación").append(" | ");
                    continue;
                }

                if (responseLoginDTO.getBody().getClass() == LoginResponseDto.class) {
                    LoginResponseDto loginDTOResponse = (LoginResponseDto) responseLoginDTO.getBody();

                    GroupConfirmaTransBffRequestDto confirmacion = new GroupConfirmaTransBffRequestDto();
                    confirmacion.setListConfirmacionTransferencia(iteraccion.getListConfirmaciones());
                    ResponseDTO<?> responseConfirmaciones = apiService.confirmacionTranferencia(loginDTOResponse.getKey(), apiKey, confirmacion);

                    if(responseConfirmaciones.getBody() == null) {
                        mensajeFinal.append("No hubo respuesta en confirmación de transferencias").append(" | ");
                        continue;
                    }

                    if (responseConfirmaciones.getBody().getClass() == GroupConfirmaTransResponseDto.class) {
                        GroupConfirmaTransResponseDto response = (GroupConfirmaTransResponseDto) responseConfirmaciones.getBody();
                        if (response.getStatus().equals(Constante.KEY_SUCCESS_CODE)) {
                            Map<Long, ConfirmaTransRequestDto> mapaConfirmacion = iteraccion.getListConfirmaciones().stream()
                                    .collect(Collectors.toMap(
                                            ConfirmaTransRequestDto::getIdAbonoSolicitud,
                                            Function.identity()
                                    ));

                            for (ConfirmaTransGetResponseDto responseConfirmacionIndividual : response.getListRespuestaConfirmacionTransferencia()) {
                                actualizarDatosConfirmacion(responseConfirmacionIndividual,
                                        usuarioEjecucion,
                                        fechaOperacion,
                                        ipOperacion,
                                        terminalOperacion);

                                Optional<AbonosSolicitudEntity> abonoOpt = abonosSolicitudRepository.findById(responseConfirmacionIndividual.getIdAbonoSolicitud());
                                String moneda;
                                String descripcionMoneda;
                                String codigoEntidadFinanciera;
                                String cciDestino;

                                if (abonoOpt.isPresent()) {
                                    AbonosSolicitudEntity abono = abonoOpt.get();
                                    moneda = abono.getMoneda();
                                    descripcionMoneda = "PEN".equals(moneda) ? "Soles" : "Dólares";
                                    codigoEntidadFinanciera = abono.getCodigoEntidadFinanciera();
                                    cciDestino = abono.getCuentaDestino();
                                } else {
                                    throw new ErrorControladoException("Abono no encontrado con ID: " + responseConfirmacionIndividual.getIdAbonoSolicitud());
                                }

                                if (responseConfirmacionIndividual.getStatus().equals(Constante.KEY_SUCCESS_CODE)) {
                                    registrosProcesados++;
                                    registrosPendientes = registrosTotales - (registrosErroneos + registrosProcesados);

                                    cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                                    cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                                    cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                                    cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                                    cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                                    cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                                    cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_PROCESANDO);
                                    cabeceraEjecucionRequest.setProceso(Constante.PROCESO_CONFIRMACION);
                                    cabeceraEjecucionRequest.setDetalleEjecucion(ConstanteError.MENSAJE_PROCESANDO);
                                    cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                                    cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                                    cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                                    cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                                    cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                                    DetalleEjecucionRequestDto detalleEjecucion = new DetalleEjecucionRequestDto();
                                    detalleEjecucion.setCodigoCabeceraEjecucion(cabeceraEjecucionResponse.getId());
                                    detalleEjecucion.setNumeroCuenta(cciDestino);
                                    detalleEjecucion.setMonedaCuenta(moneda);
                                    detalleEjecucion.setDescripcionMoneda(descripcionMoneda);
                                    detalleEjecucion.setCodigoEntidadFinanciera(codigoEntidadFinanciera);
                                    detalleEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_DET_PROCESADO);
                                    detalleEjecucion.setDetalleEjecucion("Confirmación procesada exitosamente");
                                    detalleEjecucion.setFechaAuditoria(LocalDateTime.now());
                                    detalleEjecucion.setUsuarioAuditoria(usuarioEjecucion);
                                    detalleEjecucion.setIpAuditoria(ipOperacion);
                                    detalleEjecucion.setTerminalAuditoria(terminalOperacion);
                                    detalleEjecucionService.registrar(detalleEjecucion);
                                } else {
                                    ConfirmaTransRequestDto request = mapaConfirmacion.get(responseConfirmacionIndividual.getIdAbonoSolicitud());
                                    if (request != null) {
                                        String mensaje = String.format(
                                                "Observacion en la operacion de la cuenta %s con importe %s: %s",
                                                request.getCuentaBaaS(),
                                                request.getImporte(),
                                                responseConfirmacionIndividual.getMessage()
                                        );

                                        mensajeFinal.append(mensaje).append(" | ");

                                        registrosErroneos++;
                                        registrosPendientes = registrosTotales - (registrosErroneos + registrosProcesados);

                                        cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                                        cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                                        cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                                        cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                                        cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                                        cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                                        cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_PROCESANDO);
                                        cabeceraEjecucionRequest.setProceso(Constante.PROCESO_CONFIRMACION);
                                        cabeceraEjecucionRequest.setDetalleEjecucion(ConstanteError.MENSAJE_PROCESANDO);
                                        cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                                        cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                                        cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                                        cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                                        cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                                        DetalleEjecucionRequestDto detalleEjecucion = new DetalleEjecucionRequestDto();
                                        detalleEjecucion.setCodigoCabeceraEjecucion(cabeceraEjecucionResponse.getId());
                                        detalleEjecucion.setNumeroCuenta(cciDestino);
                                        detalleEjecucion.setMonedaCuenta(moneda);
                                        detalleEjecucion.setDescripcionMoneda(descripcionMoneda);
                                        detalleEjecucion.setCodigoEntidadFinanciera(codigoEntidadFinanciera);
                                        detalleEjecucion.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_DET_ERRONEO);
                                        String truncado;
                                        if(responseConfirmacionIndividual.getEstado().equals(Constante.ESTADO_ALFIN_CONF_ERROR)){
                                            truncado = "Error de configuración para acceso a la cuenta de cargo";
                                        } else if(responseConfirmacionIndividual.getEstado().equals(Constante.ESTADO_ALFIN_SEG_ERROR)){
                                            truncado = "Error de seguridad";
                                        } else if(responseConfirmacionIndividual.getEstado().equals(Constante.ESTADO_ALFIN_PLAT_ERROR)){
                                            truncado = "Error en la plataforma del banco";
                                        } else {
                                            truncado = mensaje.length() > 2000 ? mensaje.substring(0, 2000) : mensaje;
                                        }
                                        detalleEjecucion.setDetalleEjecucion(truncado);
                                        detalleEjecucion.setFechaAuditoria(LocalDateTime.now());
                                        detalleEjecucion.setUsuarioAuditoria(usuarioEjecucion);
                                        detalleEjecucion.setIpAuditoria(ipOperacion);
                                        detalleEjecucion.setTerminalAuditoria(terminalOperacion);
                                        detalleEjecucionService.registrar(detalleEjecucion);
                                    }
                                }
                            }
                        } else {
                            mensajeFinal.append("Ocurrio un problema al realizar confirmacion de transferencias: ")
                                    .append(response.getMessage())
                                    .append(Constante.SEPARADOR_ERRORES);
                        }
                    }
                }
            }
            if (mensajeFinal.isEmpty()) {
                registrosPendientes = 0;

                cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO);
                cabeceraEjecucionRequest.setProceso(Constante.PROCESO_CONFIRMACION);
                cabeceraEjecucionRequest.setDetalleEjecucion("Confirmación procesada exitosamente");
                cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                ejecucionResponse.setStatus(Constante.KEY_SUCCESS_CODE);
            } else {
                registrosPendientes = 0;

                cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
                cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
                cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
                cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
                cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
                cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
                cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO_ERROR);
                cabeceraEjecucionRequest.setProceso(Constante.PROCESO_CONFIRMACION);
                String truncado = mensajeFinal.toString().length() > 2000 ? mensajeFinal.substring(0, 2000) : mensajeFinal.toString();
                cabeceraEjecucionRequest.setDetalleEjecucion(truncado);
                cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
                cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
                cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
                cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
                cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

                ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
                ejecucionResponse.setMessage(mensajeFinal.toString());
            }
        } catch (Exception e) {
            registrosPendientes = 0;

            cabeceraEjecucionRequest = new CabeceraEjecucionRequestDto();
            cabeceraEjecucionRequest.setFechaFinProceso(LocalDateTime.now());
            cabeceraEjecucionRequest.setRegistrosTotales(registrosTotales);
            cabeceraEjecucionRequest.setRegistrosProcesados(registrosProcesados);
            cabeceraEjecucionRequest.setRegistrosPendientes(registrosPendientes);
            cabeceraEjecucionRequest.setRegistrosErroneos(registrosErroneos);
            cabeceraEjecucionRequest.setEstadoProcesamiento(Constante.ESTADO_PROCESAMIENTO_CAB_FINALIZADO_ERROR);
            cabeceraEjecucionRequest.setProceso(Constante.PROCESO_CONFIRMACION);
            cabeceraEjecucionRequest.setDetalleEjecucion("Error no controlado al realizar confirmacion de transferencias");
            cabeceraEjecucionRequest.setFechaAuditoria(LocalDateTime.now());
            cabeceraEjecucionRequest.setUsuarioAuditoria(usuarioEjecucion);
            cabeceraEjecucionRequest.setIpAuditoria(ipOperacion);
            cabeceraEjecucionRequest.setTerminalAuditoria(terminalOperacion);
            cabeceraEjecucionService.actualizar(cabeceraEjecucionResponse.getId(), cabeceraEjecucionRequest);

            ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
            ejecucionResponse.setMessage("Error no controlado al realizar confirmacion de transferencias: " + e.getMessage());
        }

        return ejecucionResponse;
    }

    private void actualizarDatosConsulta(ConsultaTransGetResponseDto responseConsultaIndividual,
                                         String usuarioEjecucion,
                                         LocalDateTime fechaOperacion,
                                         String ipOperacion,
                                         String terminalOperacion) {
        try {
            AbonosSolicitudEntity abono = abonosSolicitudRepository.findByIdAndEstadoRegistro(
                    responseConsultaIndividual.getIdAbonoSolicitud(),
                    Constante.ESTADO_ACTIVO);

            abono.setTipoDocBeneficiarioRespuesta(responseConsultaIndividual.getTipoDocBeneficiario());
            abono.setDocumentoBeneficiarioRespuesta(responseConsultaIndividual.getDocumentoBeneficiario());
            abono.setNombreBeneficiarioRespuesta(responseConsultaIndividual.getNombreBeneficiario());
            abono.setDireccionBeneficiarioRespuesta(responseConsultaIndividual.getDireccionBeneficiario());
            abono.setTelefonoBeneficiarioRespuesta(responseConsultaIndividual.getTelefonoBeneficiario());
            abono.setMovilBeneficiarioRespuesta(responseConsultaIndividual.getMovilBeneficiario());
            abono.setMismoTitularOut(responseConsultaIndividual.getMismoTitularOut());
            abono.setTransferenciaId(responseConsultaIndividual.getTransferenciaId());
            abono.setItf(responseConsultaIndividual.getItf());
            abono.setComisionOrigen(responseConsultaIndividual.getComisionOrigen());
            abono.setComisionDestino(responseConsultaIndividual.getComisionDestino());
            abono.setMpe001idl(responseConsultaIndividual.getMpe001idl());
            abono.setCodRespuestaConsulta(responseConsultaIndividual.getCodRespuesta());
            abono.setDscRespuestaConsulta(responseConsultaIndividual.getDscRespuesta());
            abono.setEstadoEjecucionConsulta(responseConsultaIndividual.getEstado());
            abono.setFechaConsulta(responseConsultaIndividual.getFecha());
            abono.setHoraConsulta(responseConsultaIndividual.getHora());

            abono.setAudiUsuMod(usuarioEjecucion);
            abono.setAudiFechaMod(fechaOperacion);
            abono.setAudiIpMod(ipOperacion);
            abono.setAudiNomTerminalMod(terminalOperacion);

            abonosSolicitudRepository.save(abono);
        } catch (Exception e) {
            log.error("Error al registrar respuesta consulta Abono Solicitud: {}", e.getMessage());
        }
    }

    private void actualizarDatosConfirmacion(ConfirmaTransGetResponseDto responseConfirmacionindividual,
                                             String usuarioEjecucion,
                                             LocalDateTime fechaOperacion,
                                             String ipOperacion,
                                             String terminalOperacion) {
        try {
            AbonosSolicitudEntity abono = abonosSolicitudRepository.findByIdAndEstadoRegistro(
                    responseConfirmacionindividual.getIdAbonoSolicitud(),
                    Constante.ESTADO_ACTIVO);
            abono.setMovimientoUid(responseConfirmacionindividual.getMovimientoUid());
            abono.setCodRespuestaTransferencia(responseConfirmacionindividual.getCodRespuesta());
            abono.setDscRespuestaTransferencia(responseConfirmacionindividual.getDscRespuesta());
            abono.setEstadoEjecucionTransferencia(responseConfirmacionindividual.getEstado());
            abono.setFechaTransferencia(responseConfirmacionindividual.getFecha());
            abono.setHoraTransferencia(responseConfirmacionindividual.getHora());

            abono.setAudiUsuMod(usuarioEjecucion);
            abono.setAudiFechaMod(fechaOperacion);
            abono.setAudiIpMod(ipOperacion);
            abono.setAudiNomTerminalMod(terminalOperacion);

            abonosSolicitudRepository.save(abono);
        } catch (Exception e) {
            log.error("Error al registrar respuesta de confirmacion Abono Solicitud: {}", e.getMessage());
        }
    }
}
