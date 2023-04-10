package com.dhsp.luvu.dto.request;

public class CollectionRequest {
    private String name;
    private Long imageID;

    public CollectionRequest() {
    }

    public CollectionRequest(String name) {
        this.name = name;
    }

    public CollectionRequest(String name, Long imageID) {
        this.name = name;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getImageID() {
        return imageID;
    }

    public void setImageID(Long imageID) {
        this.imageID = imageID;
    }
}
