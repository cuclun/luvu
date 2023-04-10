package com.dhsp.luvu.dto.response;

public class SpecificationResponse {
    private Long id;
    private String name;

    public SpecificationResponse() {
    }

    public SpecificationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
