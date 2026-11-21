package com.ekjot.orderprocessing.enums;

public enum ShipmentStatus {
    CREATED,
    PACKED,
    SHIPPED,
    DELIVERED,
    RETURNED;

    public boolean canTransitionTo(ShipmentStatus next){
        return switch (this){
            case CREATED -> next == PACKED;
            case PACKED -> next == SHIPPED;
            case SHIPPED -> next == DELIVERED || next == RETURNED;
            case DELIVERED, RETURNED -> false;
        };
    }
}
