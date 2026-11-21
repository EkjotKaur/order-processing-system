package com.ekjot.orderprocessing.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {
    @NotNull
    private Long customerId;

    @NotEmpty
    @Valid
    private List<CreateOrderItemRequest> items;

    @NotNull
    @Valid
    private ShippingAddressRequest shippingAddress;

    public List<CreateOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateOrderItemRequest> items) {
        this.items = items;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public ShippingAddressRequest getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressRequest shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
