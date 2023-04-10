package com.dhsp.luvu.dto.request;

public class CartProductRequest {
    private Long id;
    private String size;
    private String color;
    private int qty;

    public CartProductRequest() {
    }

    public CartProductRequest(Long id, String size, String color, int qty) {
        this.id = id;
        this.size = size;
        this.color = color;
        this.qty = qty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "CartProductRequest{" +
                "id=" + id +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", qty=" + qty +
                '}';
    }
}
