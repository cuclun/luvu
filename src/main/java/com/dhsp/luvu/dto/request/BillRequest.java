package com.dhsp.luvu.dto.request;

import java.util.List;

public class BillRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String paymentMethod;
    private List<CartProductRequest> listProduct;

    public BillRequest(String name, String email, String phone, String address, String paymentMethod, List<CartProductRequest> listProduct) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.listProduct = listProduct;
    }

    public BillRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<CartProductRequest> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<CartProductRequest> listProduct) {
        this.listProduct = listProduct;
    }

    @Override
    public String toString() {
        return "BillRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", listProduct=" + listProduct +
                '}';
    }
}
