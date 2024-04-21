package com.AntonioBarrios.ventas.ventas.service;

import com.AntonioBarrios.ventas.ventas.model.Client;
import com.AntonioBarrios.ventas.ventas.model.Product;
import com.AntonioBarrios.ventas.ventas.model.Ventas;
import com.AntonioBarrios.ventas.ventas.repository.VentasRepository;
import com.AntonioBarrios.ventas.RabbitMQConfig;
import com.AntonioBarrios.ventas.rabbit.dto.StockUpdateMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.time.Instant;

@Service

public class VentasService {
    private static final Logger log = LoggerFactory.getLogger(VentasService.class);

    private final WebClient.Builder webClientBuilder;
    private final String clientsServiceUrl;
    private final String productsServiceUrl;
    private final VentasRepository ventasRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public VentasService(WebClient.Builder webClientBuilder,
            @Value("${clients.service.url}") String clientsServiceUrl,
            @Value("${products.service.url}") String productsServiceUrl,
            VentasRepository ventasRepository,
            RabbitTemplate rabbitTemplate) {
        this.webClientBuilder = webClientBuilder;
        this.clientsServiceUrl = clientsServiceUrl;
        this.productsServiceUrl = productsServiceUrl;
        this.ventasRepository = ventasRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Client getClient(Long clientId) {
        return webClientBuilder.build()
                .get()
                .uri(clientsServiceUrl + "/clients/" + clientId)
                .retrieve()
                .bodyToMono(Client.class)
                .block();
    }

    public Product getProduct(Long productId) {
        return webClientBuilder.build()
                .get()
                .uri(productsServiceUrl + "/products/" + productId)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }

    public Ventas addVenta(Ventas venta, boolean isAddOperation) {
        Client client = getClient(venta.getClient().getId());
        venta.setClient(client);
        Product product = getProduct(venta.getProduct().getId());
        if (product.getStock() < venta.getQuantity()) {
            throw new RuntimeException("Stock insuficiente para el producto ID: " + product.getId());
        }
        venta.setProduct(product);
        venta.setSaleTime(Instant.now()); // Establece el momento actual de la venta
        venta = ventasRepository.save(venta);
        sendStockUpdate(product.getId(), venta.getQuantity(),
                isAddOperation ? RabbitMQConfig.ADD_STOCK_ROUTING_KEY : RabbitMQConfig.SUBTRACT_STOCK_ROUTING_KEY);
        return venta;
    }

    private void sendStockUpdate(Long productId, int quantity, String routingKey) {
        StockUpdateMessage stockUpdateMessage = new StockUpdateMessage(productId, quantity,
                routingKey.equals(RabbitMQConfig.ADD_STOCK_ROUTING_KEY));
        log.info("Enviando actualización de stock: {}", stockUpdateMessage);
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_EXCHANGE, routingKey, stockUpdateMessage);
            log.info("Actualización de stock enviada exitosamente al exchange: {} con routing key: {}",
                    RabbitMQConfig.STOCK_EXCHANGE, routingKey);
        } catch (Exception e) {
            log.error("Error al enviar actualización de stock: {}", e.getMessage(), e);
        }
    }

    public List<Ventas> findAllVentas() {
        return ventasRepository.findAll();
    }
}