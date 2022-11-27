package edu.nraj.sagaorchestrator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Configuration
public class MessagingQueueServiceConfig {
    static final String topicExchangeName = "amqp.topic";

    static final String sagaQueue = "saga-queue";
    static final String kitchenQueue = "kitchen-queue";
    static final String consumerQueue = "consumer-queue";
    static final String accountingQueue = "accounting-queue";
    static final String orderQueue = "order-queue";

    @Bean
    Queue sagaQueue() {
        return new Queue(sagaQueue, false);
    }

    @Bean
    Queue kitchenQueue() {
        return new Queue(kitchenQueue, false);
    }

    @Bean
    Queue consumerQueue() { return new Queue(consumerQueue, false); }

    @Bean
    Queue accountingQueue() { return new Queue(accountingQueue, false); }

    @Bean
    Queue orderQueue() { return new Queue(orderQueue, false); }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding sagaBinding() {
        return BindingBuilder.bind(sagaQueue()).to(exchange()).with("saga");
    }

    @Bean
    Binding kitchenBinding() {
        return BindingBuilder.bind(kitchenQueue()).to(exchange()).with("kitchen");
    }

    @Bean
    Binding consumerBinding() {
        return BindingBuilder.bind(consumerQueue()).to(exchange()).with("consumer");
    }

    @Bean
    Binding accountingBinding() {
        return BindingBuilder.bind(accountingQueue()).to(exchange()).with("accounting");
    }

    @Bean
    Binding orderBinding() { return BindingBuilder.bind(accountingQueue()).to(exchange()).with("order"); }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    MessageConverter converter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonConverter());
        factory.setDefaultRequeueRejected(false); // This is needed for the failed message to make it to DLQ
        return factory;
    }

    private MessageConverter jsonConverter() {
        val objectMapper = new ObjectMapper();
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
