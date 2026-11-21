package com.ekjot.orderprocessing.service;

import com.ekjot.orderprocessing.dto.request.CreateShipmentRequest;
import com.ekjot.orderprocessing.dto.response.ShipmentResponse;
import com.ekjot.orderprocessing.entity.Order;
import com.ekjot.orderprocessing.entity.Shipment;
import com.ekjot.orderprocessing.enums.FulfillmentStatus;
import com.ekjot.orderprocessing.enums.OrderStatus;
import com.ekjot.orderprocessing.enums.ShipmentStatus;
import com.ekjot.orderprocessing.exception.InvalidOrderStateException;
import com.ekjot.orderprocessing.exception.InvalidShipmentStateException;
import com.ekjot.orderprocessing.exception.ShipmentAlreadyExistsException;
import com.ekjot.orderprocessing.exception.ShipmentNotFoundException;
import com.ekjot.orderprocessing.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderService orderService;

    private Shipment getShipmentByOrderId(Long orderId){
        return shipmentRepository.findByOrderId(orderId).orElseThrow(() -> new ShipmentNotFoundException(orderId));
    }

    private Shipment updateShipmentStatus(Long orderId, ShipmentStatus status){
        Shipment shipment = getShipmentByOrderId(orderId);
        if(!shipment.getStatus().canTransitionTo(status)){
            throw new InvalidShipmentStateException(orderId, shipment.getStatus());
        }
        shipment.setStatus(status);
        shipment.setUpdatedAt(LocalDateTime.now());
        return shipment;
    }

    private ShipmentResponse mapToShipmentResponse(Shipment shipment){
        return new ShipmentResponse(shipment.getId(), shipment.getOrderId(), shipment.getStatus(), shipment.getCarrier(), shipment.getTrackingNumber(), shipment.getShippedAt(), shipment.getDeliveredAt(), shipment.getCreatedAt(), shipment.getUpdatedAt());
    }

    public ShipmentService(ShipmentRepository shipmentRepository, OrderService orderService){
        this.shipmentRepository = shipmentRepository;
        this.orderService = orderService;
    }

    @Transactional
    public ShipmentResponse createShipment(CreateShipmentRequest request){
        Long orderId = request.getOrderId();
        Order order = orderService.getOrderById(orderId);
        if(order.getStatus() != OrderStatus.CONFIRMED){
            throw new InvalidOrderStateException(orderId, order.getStatus());
        }

        boolean shipmentExist = shipmentRepository.findByOrderId(orderId).isPresent();

        if(shipmentExist){
            throw new ShipmentAlreadyExistsException(orderId);
        }

        Shipment shipment = new Shipment();
        shipment.setCarrier(request.getCarrier());
        shipment.setTrackingNumber(request.getTrackingNumber());
        LocalDateTime now = LocalDateTime.now();

        shipment.setOrderId(orderId);
        shipment.setCreatedAt(now);
        shipment.setUpdatedAt(now);

        shipmentRepository.save(shipment);
        return mapToShipmentResponse(shipment);
    }

    public ShipmentResponse getShipment(Long orderId){
        Shipment shipment = getShipmentByOrderId(orderId);
        return mapToShipmentResponse(shipment);
    }

    @Transactional
    public ShipmentResponse packShipment(Long orderId){
        Shipment shipment = updateShipmentStatus(orderId, ShipmentStatus.PACKED);
        shipmentRepository.save(shipment);
        return mapToShipmentResponse(shipment);
    }

    @Transactional
    public ShipmentResponse shipShipment(Long orderId){
        Shipment shipment = updateShipmentStatus(orderId, ShipmentStatus.SHIPPED);
        orderService.updateFulfillmentStatus(orderId, FulfillmentStatus.SHIPPED);
        shipment.setShippedAt(LocalDateTime.now());
        shipmentRepository.save(shipment);
        return mapToShipmentResponse(shipment);
    }

    @Transactional
    public ShipmentResponse deliverShipment(Long orderId){
        Shipment shipment = updateShipmentStatus(orderId, ShipmentStatus.DELIVERED);
        orderService.updateFulfillmentStatus(orderId, FulfillmentStatus.DELIVERED);
        shipment.setDeliveredAt(LocalDateTime.now());
        shipmentRepository.save(shipment);
        return mapToShipmentResponse(shipment);
    }

    @Transactional
    public ShipmentResponse returnShipment(Long orderId){
        Shipment shipment = updateShipmentStatus(orderId, ShipmentStatus.RETURNED);
        orderService.updateFulfillmentStatus(orderId, FulfillmentStatus.RETURNED);
        shipmentRepository.save(shipment);
        return mapToShipmentResponse(shipment);
    }
}
