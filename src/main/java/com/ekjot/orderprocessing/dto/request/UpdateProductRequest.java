package com.ekjot.orderprocessing.dto.request;

import com.ekjot.orderprocessing.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProductRequest {
    @NotBlank
    @Size(max=255)
    private String name;

    @Size(max=1000)
    private String description;

    private ProductStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
