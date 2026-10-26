package com.ekjot.orderprocessing.dto.response;

import com.ekjot.orderprocessing.enums.ProductStatus;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private ProductStatus status;

    public ProductResponse(Long id, String name, String description, ProductStatus status){
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
