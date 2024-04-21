package com.AntonioBarrios.ventas.rabbit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.AntonioBarrios.ventas.RabbitMQConfig;
import com.AntonioBarrios.ventas.ventas.service.VentasService;
import com.AntonioBarrios.ventas.rabbit.dto.StockUpdateMessage;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RabbitMQListenerService {

    private final VentasService ventasService;
    private final Logger logger = LoggerFactory.getLogger(RabbitMQListenerService.class);

    @Autowired
    public RabbitMQListenerService(VentasService ventasService) {
        this.ventasService = ventasService;
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_QUEUE)
    public void handleStockUpdate(StockUpdateMessage stockUpdateMessage) {
        logger.info("Manejando actualización de stock: {}", stockUpdateMessage);

    }
}
