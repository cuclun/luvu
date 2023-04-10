package com.dhsp.luvu.dto.request;

public class ProductRequest {
    private String name;
    private Long price;
    private String description;
    private Long collectionId;
    private int quantity;
    private Long[] imagesId;
    private String[] specifications;

    public ProductRequest() {
    }

    public ProductRequest(String name, Long price, String description, Long collectionId, int quantity, Long[] imagesId, String[] specifications) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.collectionId = collectionId;
        this.quantity = quantity;
        this.imagesId = imagesId;
        this.specifications = specifications;
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

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String[] getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String[] specifications) {
        this.specifications = specifications;
    }

    public Long[] getImagesId() {
        return imagesId;
    }

    public void setImagesId(Long[] imagesId) {
        this.imagesId = imagesId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
