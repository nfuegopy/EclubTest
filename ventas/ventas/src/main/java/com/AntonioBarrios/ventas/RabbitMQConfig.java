package com.AntonioBarrios.ventas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitMQConfig {
    public static final String STOCK_QUEUE = "stock_queue";
    public static final String STOCK_EXCHANGE = "stock_exchange";
    public static final String ADD_STOCK_ROUTING_KEY = "add_stock";
    public static final String SUBTRACT_STOCK_ROUTING_KEY = "subtract_stock";

    @Bean
    Queue queue() {
        return new Queue(STOCK_QUEUE, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(STOCK_EXCHANGE);
    }

    @Bean
    Binding bindingAdd(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ADD_STOCK_ROUTING_KEY);
    }

    @Bean
    Binding bindingSubtract(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SUBTRACT_STOCK_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory,
            final Jackson2JsonMessageConverter messageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

}
