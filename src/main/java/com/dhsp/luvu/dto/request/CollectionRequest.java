package com.dhsp.luvu.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class CollectionRequest {

    private String name;

    private MultipartFile image;

    public CollectionRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
