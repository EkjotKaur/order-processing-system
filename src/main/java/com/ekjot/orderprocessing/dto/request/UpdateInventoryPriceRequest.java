package com.ekjot.orderprocessing.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class UpdateInventoryPriceRequest {
    @NotNull
    private Long productId;

    private BigDecimal price;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
