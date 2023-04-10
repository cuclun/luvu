package com.dhsp.luvu.dto.response;

import com.dhsp.luvu.entity.Product;

public class BillDetailsResponse {
    private Product product;
    private String color;
    private int quantity;
    private String size;

    public BillDetailsResponse(Product product, String color, int quantity, String size) {
        this.product = product;
        this.color = color;
        this.quantity = quantity;
        this.size = size;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
