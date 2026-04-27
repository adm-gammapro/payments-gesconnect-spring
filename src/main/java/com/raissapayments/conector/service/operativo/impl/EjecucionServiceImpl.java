package com.raissapayments.conector.service.operativo.impl;

import com.raissa.comun.general.dto.ResponseDTO;
import com.raissa.comun.util.Constante;
import com.raissapayments.conector.domain.dto.commons.InstitucionFinancieraDto;
import com.raissapayments.conector.domain.dto.operativo.request.LoginAlfinRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.ConfirmaTransRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.ConsultaTransRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.EjecucionRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConfirmaCredencialesDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConfirmaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConsultaTransBffRequestDto;
import com.raissapayments.conector.domain.dto.operativo.request.ejecucion.GroupConsultasCredencialesDto;
import com.raissapayments.conector.domain.dto.operativo.response.AccountsResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.LoginResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.SaldoResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.ConfirmaTransGetResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.ConsultaTransGetResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.EjecucionResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.GroupConfirmaTransResponseDto;
import com.raissapayments.conector.domain.dto.operativo.response.ejecucion.GroupConsultaTransResponseDto;
import com.raissapayments.conector.domain.entity.operativo.AbonosSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CargoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.CuentaOrdenanteEntity;
import com.raissapayments.conector.domain.entity.operativo.RespuestaAbonoSolicitudEntity;
import com.raissapayments.conector.domain.entity.operativo.SolicitudEntity;
import com.raissapayments.conector.domain.repository.operativo.AbonosSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.CargoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.CuentaOrdenanteRepository;
import com.raissapayments.conector.domain.repository.operativo.RespuestaAbonoSolicitudRepository;
import com.raissapayments.conector.domain.repository.operativo.SolicitudRepository;
import com.raissapayments.conector.exception.operativo.ConnectionException;
import com.raissapayments.conector.service.operativo.ApiService;
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
    private final RespuestaAbonoSolicitudRepository respuestaAbonoSolicitudRepository;

    private final ApiService apiService;

    public EjecucionResponseDto consultarTransferenciaInmediata(EjecucionRequestDto request) {
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

            List<AbonosSolicitudEntity> listAbonos = abonosSolicitudRepository.findByCargoSolicitudIdAndEstadoRegistro(cargo.getId(),
                    Constante.ESTADO_ACTIVO);
            for (AbonosSolicitudEntity abono : listAbonos) {
                consulta = new ConsultaTransRequestDto();
                consulta.setIdSolicitud(request.getIdSolicitud());
                consulta.setIdCargoSolicitud(cargo.getId());
                consulta.setIdAbonoSolicitud(abono.getId());
                consulta.setClienteBaaS(cuentaOpt.getUsuarioOrdenante());
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
            loginDto.setUsername(cuentaOpt.getUsuarioOrdenante());
            loginDto.setPassword(cuentaOpt.getPasswordOrdenante());
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
                request.getIpAuditoria(),
                request.getTerminalAuditoria());

        return responseDto;
    }

    public EjecucionResponseDto confirmarTransferenciaInmediata(EjecucionRequestDto request) {
        List<ConfirmaTransRequestDto> listConfirmaciones = new ArrayList<>();
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

        ConfirmaTransRequestDto confirmacion;
        if (listCargo == null || listCargo.isEmpty()) {
            responseDto.setStatus(Constante.KEY_ERROR_CODE);
            responseDto.setMessage("No hay cargos que consultar");
            return responseDto;
        }

        List<GroupConfirmaCredencialesDto> listIteraccion = new ArrayList<>();

        for (CargoSolicitudEntity cargo : listCargo) {
            GroupConfirmaCredencialesDto iteraccion = new GroupConfirmaCredencialesDto();


            CuentaOrdenanteEntity cuentaOpt = cuentaOrdenanteRepository.findByNumeroCuentaOrdenanteAndEstadoRegistro(
                    cargo.getCuentaOrigen(),
                    Constante.ESTADO_ACTIVO
            );
            if (cuentaOpt == null) {
                continue;
            }

            List<AbonosSolicitudEntity> listAbonos = abonosSolicitudRepository.findByCargoSolicitudIdAndEstadoRegistro(cargo.getId(),
                    Constante.ESTADO_ACTIVO);
            for (AbonosSolicitudEntity abono : listAbonos) {
                confirmacion = new ConfirmaTransRequestDto();
                confirmacion.setIdSolicitud(request.getIdSolicitud());
                confirmacion.setIdCargoSolicitud(cargo.getId());
                confirmacion.setIdAbonoSolicitud(abono.getId());
                confirmacion.setClienteBaaS(cuentaOpt.getUsuarioOrdenante());
                confirmacion.setCuentaBaaS(cargo.getCuentaOrigen());

                String moneda;
                if (cargo.getMoneda().equals(Constante.CODIGO_MONEDA_SOLES_ISO)) {
                    moneda = Constante.CODIGO_MONEDA_SOLES;
                } else {
                    moneda = Constante.CODIGO_MONEDA_DOLARES;
                }
                confirmacion.setMoneda(moneda);

                confirmacion.setImporte(abono.getMontoDestino());

                RespuestaAbonoSolicitudEntity respuestaConsulta = respuestaAbonoSolicitudRepository.findByCodigoAbonoSolicitudAndEstadoRegistro(
                        abono.getId(),
                        Constante.ESTADO_ACTIVO);

                confirmacion.setTransferenciaId(respuestaConsulta.getTransferenciaId());
                confirmacion.setMpe001idl(respuestaConsulta.getMpe001idl());

                listConfirmaciones.add(confirmacion);
            }

            iteraccion.setListConfirmaciones(listConfirmaciones);

            LoginAlfinRequestDto loginDto = new LoginAlfinRequestDto();
            loginDto.setApiKey(apiKey);
            loginDto.setUsername(cuentaOpt.getUsuarioOrdenante());
            loginDto.setPassword(cuentaOpt.getPasswordOrdenante());
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
                                      request.getIpAuditoria(),
                                      request.getTerminalAuditoria());

        return responseDto;
    }

    private EjecucionResponseDto operarConsulta(List<GroupConsultasCredencialesDto> iteracciones,
                                                String apiKey,
                                                String usuarioEjecucion,
                                                LocalDateTime fechaOperacion,
                                                String ipOperacion,
                                                String terminalOperacion) {
        EjecucionResponseDto ejecucionResponse = new EjecucionResponseDto();
        StringBuilder mensajeFinal = new StringBuilder();

        try {
            for (GroupConsultasCredencialesDto iteraccion : iteracciones) {
                ResponseDTO<?> responseLoginDTO = apiService.login(iteraccion.getLoginAlfin());

                if (responseLoginDTO.getBody().getClass() == LoginResponseDto.class) {
                    LoginResponseDto loginDTOResponse = (LoginResponseDto) responseLoginDTO.getBody();

                    GroupConsultaTransBffRequestDto consultas = new GroupConsultaTransBffRequestDto();
                    consultas.setListConsultaTransferencia(iteraccion.getListConsulta());

                    ResponseDTO<?> responseSaldoDTO = apiService.saldos(loginDTOResponse.getKey(),
                            apiKey,
                            iteraccion.getLoginAlfin().getUsername(),
                            iteraccion.getNumeroCuentaCargo());

                    if (responseSaldoDTO == null) {
                        mensajeFinal.append("No se pudo recuperar saldo de cuenta: ")
                                .append(iteraccion.getNumeroCuentaCargo())
                                .append(Constante.SEPARADOR_ERRORES);
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
                                continue;
                            }
                        }
                    }

                    ResponseDTO<?> responseConsultas = apiService.consultaTranferencia(loginDTOResponse.getKey(), apiKey, consultas);

                    if (responseConsultas.getBody().getClass() == GroupConsultaTransResponseDto.class) {
                        GroupConsultaTransResponseDto response = (GroupConsultaTransResponseDto) responseConsultas.getBody();
                        if (response.getStatus().equals(Constante.KEY_SUCCESS_CODE)) {
                            Map<Long, ConsultaTransRequestDto> mapaConsulta = iteraccion.getListConsulta().stream()
                                    .collect(Collectors.toMap(
                                            ConsultaTransRequestDto::getIdAbonoSolicitud,
                                            Function.identity()
                                    ));

                            for (ConsultaTransGetResponseDto responseConsultaindividual : response.getListRespuestaConsultaTransferencia()) {
                                if (responseConsultaindividual.getStatus().equals(Constante.KEY_ERROR_CODE)) {
                                    ConsultaTransRequestDto request = mapaConsulta.get(responseConsultaindividual.getIdAbonoSolicitud());
                                    if (request != null) {
                                        String mensaje = String.format(
                                                "Observacion en la operacion de la cuenta %s con importe %s: %s",
                                                request.getCciBeneficiario(),
                                                request.getImporte(),
                                                responseConsultaindividual.getMessage()
                                        );

                                        mensajeFinal.append(mensaje).append(Constante.SEPARADOR_ERRORES);
                                    }

                                } else {
                                    registrarOperacion(responseConsultaindividual,
                                            usuarioEjecucion,
                                            fechaOperacion,
                                            ipOperacion,
                                            terminalOperacion);
                                }
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
                ejecucionResponse.setStatus(Constante.KEY_SUCCESS_CODE);
            } else {
                ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
                ejecucionResponse.setMessage(mensajeFinal.toString());
            }
        } catch (ConnectionException e){
            ejecucionResponse.setStatus(Constante.KEY_NOT_ACTION_CODE);
        } catch (Exception e) {
            ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
            ejecucionResponse.setMessage("Error no controlado al realizar consulta de transferencias: " + e.getMessage());
        }

        return ejecucionResponse;
    }

    private EjecucionResponseDto operarConfirmar(List<GroupConfirmaCredencialesDto> iteracciones,
                                                 String apiKey,
                                                 String usuarioEjecucion,
                                                 LocalDateTime fechaOperacion,
                                                 String ipOperacion,
                                                 String terminalOperacion) {
        EjecucionResponseDto ejecucionResponse = new EjecucionResponseDto();
        StringBuilder mensajeFinal = new StringBuilder();

        try {
            for (GroupConfirmaCredencialesDto iteraccion : iteracciones) {
                ResponseDTO<?> responseLoginDTO = apiService.login(iteraccion.getLoginAlfin());

                if (responseLoginDTO.getBody().getClass() == LoginResponseDto.class) {
                    LoginResponseDto loginDTOResponse = (LoginResponseDto) responseLoginDTO.getBody();

                    GroupConfirmaTransBffRequestDto confirmacion = new GroupConfirmaTransBffRequestDto();
                    confirmacion.setListConfirmacionTransferencia(iteraccion.getListConfirmaciones());
                    ResponseDTO<?> responseConfirmaciones = apiService.confirmacionTranferencia(loginDTOResponse.getKey(), apiKey, confirmacion);

                    if (responseConfirmaciones.getBody().getClass() == GroupConfirmaTransResponseDto.class) {
                        GroupConfirmaTransResponseDto response = (GroupConfirmaTransResponseDto) responseConfirmaciones.getBody();
                        if (response.getStatus().equals(Constante.KEY_SUCCESS_CODE)) {
                            Map<Long, ConfirmaTransRequestDto> mapaConfirmacion = iteraccion.getListConfirmaciones().stream()
                                    .collect(Collectors.toMap(
                                            ConfirmaTransRequestDto::getIdAbonoSolicitud,
                                            Function.identity()
                                    ));

                            for (ConfirmaTransGetResponseDto responseConfirmacionindividual : response.getListRespuestaConfirmacionTransferencia()) {
                                registrarConfirmacionOperacion(responseConfirmacionindividual,
                                        usuarioEjecucion,
                                        fechaOperacion,
                                        ipOperacion,
                                        terminalOperacion);
                                if (responseConfirmacionindividual.getStatus().equals(Constante.KEY_ERROR_CODE)) {
                                    ConfirmaTransRequestDto request = mapaConfirmacion.get(responseConfirmacionindividual.getIdAbonoSolicitud());
                                    if (request != null) {
                                        String mensaje = String.format(
                                                "Observacion en la operacion de la cuenta %s con importe %s: %s",
                                                request.getCuentaBaaS(),
                                                request.getImporte(),
                                                responseConfirmacionindividual.getMessage()
                                        );

                                        mensajeFinal.append(mensaje).append(" | ");
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
            if(mensajeFinal.isEmpty()) {
                ejecucionResponse.setStatus(Constante.KEY_SUCCESS_CODE);
            } else {
                ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
                ejecucionResponse.setMessage(mensajeFinal.toString());
            }
        } catch (Exception e) {
            ejecucionResponse.setStatus(Constante.KEY_ERROR_CODE);
            ejecucionResponse.setMessage("Error no controlado al realizar confirmacion de transferencias: " + e.getMessage());
        }

        return ejecucionResponse;
    }

    private void registrarOperacion(ConsultaTransGetResponseDto responseConsultaindividual,
                                    String usuarioEjecucion,
                                    LocalDateTime fechaOperacion,
                                    String ipOperacion,
                                    String terminalOperacion){
        try {
            RespuestaAbonoSolicitudEntity respuesta = new RespuestaAbonoSolicitudEntity();
            respuesta.setCodigoSolicitud(responseConsultaindividual.getIdSolicitud());
            respuesta.setCodigoCargoSolicitud(responseConsultaindividual.getIdCargoSolicitud());
            respuesta.setCodigoAbonoSolicitud(responseConsultaindividual.getIdAbonoSolicitud());
            respuesta.setTipoDocBeneficiario(responseConsultaindividual.getTipoDocBeneficiario());
            respuesta.setDocumentoBeneficiario(responseConsultaindividual.getDocumentoBeneficiario());
            respuesta.setNombreBeneficiario(responseConsultaindividual.getNombreBeneficiario());
            respuesta.setDireccionBeneficiario(responseConsultaindividual.getDireccionBeneficiario());
            respuesta.setTelefonoBeneficiario(responseConsultaindividual.getTelefonoBeneficiario());
            respuesta.setMovilBeneficiario(responseConsultaindividual.getMovilBeneficiario());
            respuesta.setMismoTitularOut(responseConsultaindividual.getMismoTitularOut());
            respuesta.setTransferenciaId(responseConsultaindividual.getTransferenciaId());
            respuesta.setItf(responseConsultaindividual.getItf());
            respuesta.setComisionOrigen(responseConsultaindividual.getComisionOrigen());
            respuesta.setComisionDestino(responseConsultaindividual.getComisionDestino());
            respuesta.setMpe001idl(responseConsultaindividual.getMpe001idl());
            respuesta.setCodRespuestaConsulta(responseConsultaindividual.getCodRespuesta());
            respuesta.setDscRespuestaConsulta(responseConsultaindividual.getDscRespuesta());
            respuesta.setErrorConsulta(responseConsultaindividual.getMessage());
            respuesta.setEstadoEjecucionConsulta(responseConsultaindividual.getEstado());
            respuesta.setFechaConsulta(responseConsultaindividual.getFecha());
            respuesta.setHoraConsulta(responseConsultaindividual.getHora());

            respuesta.setEstadoRegistro(Constante.ESTADO_ACTIVO);
            respuesta.setAudiUsuario(usuarioEjecucion);
            respuesta.setAudiFechIns(fechaOperacion);
            respuesta.setAudiIp(ipOperacion);
            respuesta.setAudiNomTerminal(terminalOperacion);

            respuestaAbonoSolicitudRepository.save(respuesta);
        } catch (Exception e) {
            log.error("Error al registrar respuesta consulta Abono Solicitud: {}", e.getMessage());
        }
    }

    private void registrarConfirmacionOperacion(ConfirmaTransGetResponseDto responseConfirmacionindividual,
                                                String usuarioEjecucion,
                                                LocalDateTime fechaOperacion,
                                                String ipOperacion,
                                                String terminalOperacion){
        try {
            RespuestaAbonoSolicitudEntity respuesta = new RespuestaAbonoSolicitudEntity();
            respuesta.setMovimientoUid(responseConfirmacionindividual.getMovimientoUid());
            respuesta.setCodRespuestaTransferencia(responseConfirmacionindividual.getCodRespuesta());
            respuesta.setDscRespuestaTransferencia(responseConfirmacionindividual.getDscRespuesta());
            respuesta.setErrorConsulta(responseConfirmacionindividual.getMessage());
            respuesta.setEstadoEjecucionConsulta(responseConfirmacionindividual.getEstado());
            respuesta.setFechaConsulta(responseConfirmacionindividual.getFecha());
            respuesta.setHoraConsulta(responseConfirmacionindividual.getHora());

            respuesta.setAudiUsuMod(usuarioEjecucion);
            respuesta.setAudiFechaMod(fechaOperacion);
            respuesta.setAudiIpMod(ipOperacion);
            respuesta.setAudiNomTerminalMod(terminalOperacion);

            respuestaAbonoSolicitudRepository.save(respuesta);
        } catch (Exception e) {
            log.error("Error al registrar respuesta de confirmacion Abono Solicitud: {}", e.getMessage());
        }
    }
}
