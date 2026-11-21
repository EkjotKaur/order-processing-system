package com.ekjot.orderprocessing.dto.response;

import java.time.LocalDateTime;

public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String deviceToken;
    private LocalDateTime createdAt;

    public CustomerResponse(Long id, String name, String email, String phone, String deviceToken, LocalDateTime createdAt){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deviceToken = deviceToken;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
