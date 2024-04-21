package com.AntonioBarrios.productmanagement.rabbit.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AntonioBarrios.productmanagement.RabbitMQConfig;
import com.AntonioBarrios.productmanagement.rabbit.dto.StockUpdate;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TestController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateStock(@RequestBody StockUpdate stockUpdate) {

        String routingKey = stockUpdate.getQuantity() >= 0 ? RabbitMQConfig.ADD_STOCK_ROUTING_KEY
                : RabbitMQConfig.SUBTRACT_STOCK_ROUTING_KEY;

        rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_EXCHANGE, routingKey, stockUpdate);
        return ResponseEntity.ok("Stock update message sent to RabbitMQ");
    }
}
