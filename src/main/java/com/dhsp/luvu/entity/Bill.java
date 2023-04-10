package com.dhsp.luvu.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Bill {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateCreate;

    private String nameCustom;

    private String phoneCustom;

    private String emailCustom;

    private String addressCustom;

    private String paymentMethod;

    private Boolean approved;

    public Bill() {

    }

    public Bill(Date dateCreate, String nameCustom, String phoneCustom, String emailCustom, String addressCustom, String paymentMethod, Boolean approved) {
        this.dateCreate = dateCreate;
        this.nameCustom = nameCustom;
        this.phoneCustom = phoneCustom;
        this.emailCustom = emailCustom;
        this.addressCustom = addressCustom;
        this.paymentMethod = paymentMethod;
        this.approved = approved;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getEmailCustom() {
        return emailCustom;
    }

    public void setEmailCustom(String emailCustom) {
        this.emailCustom = emailCustom;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNameCustom() {
        return nameCustom;
    }

    public void setNameCustom(String nameCustom) {
        this.nameCustom = nameCustom;
    }

    public String getPhoneCustom() {
        return phoneCustom;
    }

    public void setPhoneCustom(String phoneCustom) {
        this.phoneCustom = phoneCustom;
    }

    public String getAddressCustom() {
        return addressCustom;
    }

    public void setAddressCustom(String addressCustom) {
        this.addressCustom = addressCustom;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
