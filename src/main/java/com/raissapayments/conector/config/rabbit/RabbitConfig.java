package com.raissapayments.conector.config.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    /**
     * Container Factory para VALIDACIÓN (rápida)
     */
    @Bean
    @Primary
    public SimpleRabbitListenerContainerFactory rabbitValidacionContainerFactory(
            ConnectionFactory connectionFactory,
            @Value("${rabbitmq.listener.validacion.concurrent-consumers:10}") int concurrentConsumers,
            @Value("${rabbitmq.listener.validacion.max-concurrent-consumers:20}") int maxConcurrentConsumers,
            @Value("${rabbitmq.listener.validacion.prefetch:5}") int prefetch,
            @Value("${rabbitmq.listener.validacion.timeout:240000}") long timeout) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        factory.setPrefetchCount(prefetch);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(false);
        factory.setReceiveTimeout(timeout);

        return factory;
    }

    /**
     * Container Factory para EJECUCIÓN (larga - 1.5 a 3 min)
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitEjecucionContainerFactory(
            ConnectionFactory connectionFactory,
            @Value("${rabbitmq.listener.ejecucion.concurrent-consumers:10}") int concurrentConsumers,
            @Value("${rabbitmq.listener.ejecucion.max-concurrent-consumers:20}") int maxConcurrentConsumers,
            @Value("${rabbitmq.listener.ejecucion.prefetch:5}") int prefetch,
            @Value("${rabbitmq.listener.ejecucion.timeout:240000}") long timeout) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(concurrentConsumers);
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        factory.setPrefetchCount(prefetch);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setDefaultRequeueRejected(false);
        factory.setReceiveTimeout(timeout);

        return factory;
    }
}
