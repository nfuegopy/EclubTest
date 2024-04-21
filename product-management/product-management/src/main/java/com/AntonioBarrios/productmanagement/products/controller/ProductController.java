package com.AntonioBarrios.productmanagement.products.controller;

import com.AntonioBarrios.productmanagement.RabbitMQConfig;
import com.AntonioBarrios.productmanagement.products.model.Product;
import com.AntonioBarrios.productmanagement.products.service.ProductService;
import com.AntonioBarrios.productmanagement.rabbit.dto.StockUpdateMessage;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService productService, RabbitTemplate rabbitTemplate) {
        this.productService = productService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productService.addProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/buy")
    public ResponseEntity<Product> buyProduct(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(productService.buyProduct(id, quantity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProductPartially(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Product updatedProduct = productService.updateProductPartially(id, updates);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<String> updateStock(@PathVariable Long id,
            @RequestBody StockUpdateMessage stockUpdateMessage) {
        logger.info("Recibiendo solicitud de actualización de stock: {}", stockUpdateMessage);
        String routingKey = stockUpdateMessage.isAddOperation() ? RabbitMQConfig.ADD_STOCK_ROUTING_KEY
                : RabbitMQConfig.SUBTRACT_STOCK_ROUTING_KEY;

        rabbitTemplate.convertAndSend(RabbitMQConfig.STOCK_EXCHANGE, routingKey, stockUpdateMessage);
        return ResponseEntity.ok("La actualización de stock fue enviada a RabbitMQ correctamente.");
    }

}
