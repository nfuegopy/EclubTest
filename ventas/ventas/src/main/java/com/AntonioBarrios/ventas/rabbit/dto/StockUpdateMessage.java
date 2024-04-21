package com.AntonioBarrios.ventas.rabbit.dto;

import java.io.Serializable;

public class StockUpdateMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long productId;
    private int quantity;
    private boolean addOperation;

    public StockUpdateMessage() {
    }

    public StockUpdateMessage(Long productId, int quantity, boolean addOperation) {
        this.productId = productId;
        this.quantity = quantity;
        this.addOperation = addOperation;
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

    public boolean isAddOperation() {
        return addOperation;
    }

    public void setAddOperation(boolean addOperation) {
        this.addOperation = addOperation;
    }
}
