package com.AntonioBarrios.ventas.rabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockUpdate {
    @JsonProperty("productId")
    private Long productId;
    @JsonProperty("quantity")
    private int quantity;

    public StockUpdate() {
    }

    public StockUpdate(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "StockUpdate{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
