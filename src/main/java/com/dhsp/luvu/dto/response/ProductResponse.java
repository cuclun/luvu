package com.dhsp.luvu.dto.response;

import java.util.List;

public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private int quantity;
    private String description;
    private List<ImageResponse> images;
    private List<SpecificationResponse> specifications;
    private Long collectionId;
    private List<String> sizes;
    private List<String> colors;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, Long price, int quantity, String description, List<ImageResponse> images, List<SpecificationResponse> specifications, Long collectionId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.images = images;
        this.specifications = specifications;
        this.collectionId = collectionId;
    }

    public ProductResponse(Long id, String name, Long price, int quantity, String description, List<ImageResponse> images, List<SpecificationResponse> specifications, Long collectionId, List<String> sizes, List<String> colors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.images = images;
        this.specifications = specifications;
        this.collectionId = collectionId;
        this.sizes = sizes;
        this.colors = colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

    public List<SpecificationResponse> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<SpecificationResponse> specifications) {
        this.specifications = specifications;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }
}
