package com.AntonioBarrios.productmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

@Configuration
public class RabbitMQErrorHandlerConfig {

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(new CustomExceptionStrategy());
    }

    public static class CustomExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {
        @Override
        public boolean isFatal(Throwable t) {
            return super.isFatal(t) || t instanceof IllegalArgumentException;
        }
    }
}