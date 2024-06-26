package com.AntonioBarrios.productmanagement.products.repository;

import com.AntonioBarrios.productmanagement.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
