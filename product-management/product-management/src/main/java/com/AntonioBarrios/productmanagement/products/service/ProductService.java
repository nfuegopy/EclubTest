package com.AntonioBarrios.productmanagement.products.service;

import com.AntonioBarrios.productmanagement.products.model.Product;
import com.AntonioBarrios.productmanagement.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Product buyProduct(Long id, int quantity) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(product.getStock() - quantity);
        return productRepository.save(product);
    }

    public Product updateProductPartially(Long id, Map<String, Object> updates) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        product.setName((String) value);
                        break;
                    case "price":
                        product.setPrice((Double) value);
                        break;
                    case "stock":
                        product.setStock((Integer) value);
                        break;
                }
            });
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado por ID: " + id));
    }

    @Transactional
    public void updateProductStock(Long productId, int quantityToSubtract) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        int updatedStock = product.getStock() - quantityToSubtract;
        if (updatedStock < 0) {
            throw new RuntimeException("Stock insufieciente en el producto ID: " + productId);
        }
        product.setStock(updatedStock);
        productRepository.save(product);
    }

    public void addStock(Long productId, int quantityToAdd) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        product.setStock(product.getStock() + quantityToAdd);
        productRepository.save(product);
    }

    public void subtractStock(Long productId, int quantityToSubtract) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        int updatedStock = product.getStock() - quantityToSubtract;
        if (updatedStock < 0) {
            throw new RuntimeException("Stock insufieciente en el producto ID: " + productId);
        }
        product.setStock(updatedStock);
        productRepository.save(product);
    }

}
