package com.dhsp.luvu.dto.request;

import org.springframework.web.multipart.MultipartFile;

public class BannerRequest {
    private MultipartFile image;

    public BannerRequest() {
    }

    public MultipartFile getImage() { return image; }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
