package com.raissapayments.conector.config.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.raissa.comun.aplicacion.dto.payments.FlujoSolicitudComunRequestDto;
import com.raissapayments.conector.config.DataSourceContextHolder;
import com.raissapayments.conector.domain.dto.commons.InstitucionFinancieraDto;
import com.raissapayments.conector.domain.dto.operativo.request.CambioEstadoRequestDto;
import com.raissapayments.conector.service.operativo.SolicitudFlujoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConsumerValidacionSolicitud {
    private final SolicitudFlujoService solicitudFlujoService;
    private final ObjectMapper objectMapper;

    @RabbitListener(
            queues = "${rabbitmq.queues.payments.consulta}",
            containerFactory = "rabbitValidacionContainerFactory",
            ackMode = "MANUAL"
    )
    public void consumirValidacion(Message message,
                                   Channel channel,
                                   @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        String json = new String(message.getBody());
        FlujoSolicitudComunRequestDto dto = objectMapper.readValue(json, FlujoSolicitudComunRequestDto.class);

        Long solicitudId = dto.getSolicitudId();
        Long codigoCliente = dto.getCodigoCliente();

        log.info("📥 Validación recibida - SolicitudId: {}, Cliente: {}", solicitudId, codigoCliente);

        try {
            if (codigoCliente != null) {
                DataSourceContextHolder.setDataSourceKey(String.valueOf(codigoCliente));
            }

            List<InstitucionFinancieraDto> listInstituciones = dto.getListInstituciones().stream()
                    .map(i->new InstitucionFinancieraDto(i.getCodigo(), i.getCodigoSbs()))
                    .toList();

            CambioEstadoRequestDto cambioEstado = new CambioEstadoRequestDto();
            cambioEstado.setSolicitudId(dto.getSolicitudId());
            cambioEstado.setUsuario(dto.getUsuario());
            cambioEstado.setCodigoCliente(dto.getCodigoCliente());
            cambioEstado.setListInstituciones(listInstituciones);
            cambioEstado.setUsuarioAuditoria(dto.getUsuarioAuditoria());
            cambioEstado.setIpAuditoria(dto.getIpAuditoria());
            cambioEstado.setTerminalAuditoria(dto.getTerminalAuditoria());

            solicitudFlujoService.validar(cambioEstado);

            channel.basicAck(tag, false);
            log.info("✅ Validación procesada exitosamente - SolicitudId: {}", solicitudId);

        } catch (Exception e) {
            log.error("❌ Error procesando validación - SolicitudId: {}", solicitudId, e);
            channel.basicNack(tag, false, false);
        } finally {
            DataSourceContextHolder.clearDataSourceKey();
        }
    }
}
