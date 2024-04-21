package com.AntonioBarrios.productmanagement.rabbit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.AntonioBarrios.productmanagement.rabbit.dto.StockUpdateMessage;
import com.AntonioBarrios.productmanagement.RabbitMQConfig;
import com.AntonioBarrios.productmanagement.products.service.ProductService;

@Service
public class RabbitMQListenerService {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQListenerService.class);

    @Autowired
    public RabbitMQListenerService(ProductService productService) {
        this.productService = productService;
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_QUEUE)
    public void handleStockUpdate(StockUpdateMessage stockUpdateMessage) {
        if (stockUpdateMessage.getProductId() == null) {
            logger.error("ProductId is null in message: " + stockUpdateMessage);
            return;
        }
        logger.info("Mensaje recibido: {}", stockUpdateMessage);
        logger.info("Actualización de stock realizada exitosamente.");
        if (stockUpdateMessage.isAddOperation()) {
            productService.addStock(stockUpdateMessage.getProductId(), stockUpdateMessage.getQuantity());
        } else {
            productService.subtractStock(stockUpdateMessage.getProductId(), stockUpdateMessage.getQuantity());
        }
    }

}