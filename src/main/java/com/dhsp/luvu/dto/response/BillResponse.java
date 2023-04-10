package com.dhsp.luvu.dto.response;

import java.util.Date;
import java.util.List;

public class BillResponse {
    private Long id;
    private List<BillDetailsResponse> billDetailsResponses;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Date date_create;
    private Boolean approved;

    public BillResponse(Long id, List<BillDetailsResponse> billDetailsResponses, String name, String address, String phone, String email, Date date_create, Boolean approved) {
        this.id = id;
        this.billDetailsResponses = billDetailsResponses;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.date_create = date_create;
        this.approved = approved;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BillDetailsResponse> getBillDetailsResponses() {
        return billDetailsResponses;
    }

    public void setBillDetailsResponses(List<BillDetailsResponse> billDetailsResponses) {
        this.billDetailsResponses = billDetailsResponses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate_create() {
        return date_create;
    }

    public void setDate_create(Date date_create) {
        this.date_create = date_create;
    }
}
