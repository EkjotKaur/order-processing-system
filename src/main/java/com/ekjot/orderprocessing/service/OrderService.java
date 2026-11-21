package com.ekjot.orderprocessing.service;

import com.ekjot.orderprocessing.dto.request.CreateOrderItemRequest;
import com.ekjot.orderprocessing.dto.request.CreateOrderRequest;
import com.ekjot.orderprocessing.dto.request.ShippingAddressRequest;
import com.ekjot.orderprocessing.dto.response.OrderItemResponse;
import com.ekjot.orderprocessing.dto.response.OrderResponse;
import com.ekjot.orderprocessing.dto.response.ShippingAddressResponse;
import com.ekjot.orderprocessing.entity.Inventory;
import com.ekjot.orderprocessing.entity.Order;
import com.ekjot.orderprocessing.entity.OrderItem;
import com.ekjot.orderprocessing.entity.ShippingAddress;
import com.ekjot.orderprocessing.enums.OrderStatus;
import com.ekjot.orderprocessing.enums.ProductStatus;
import com.ekjot.orderprocessing.exception.InvalidOrderStateException;
import com.ekjot.orderprocessing.exception.OrderNotFoundException;
import com.ekjot.orderprocessing.exception.ProductInactiveException;
import com.ekjot.orderprocessing.repository.OrderRepository;
import com.ekjot.orderprocessing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;

    private ShippingAddress mapToShippingAddress(ShippingAddressRequest shippingAddressRequest){
        return new ShippingAddress(shippingAddressRequest.getName(), shippingAddressRequest.getPhone(), shippingAddressRequest.getLine1(), shippingAddressRequest.getLine2(), shippingAddressRequest.getCity(), shippingAddressRequest.getState(), shippingAddressRequest.getCountry(), shippingAddressRequest.getPincode());
    }

    private ShippingAddressResponse mapToShippingAddressResponse(ShippingAddress shippingAddress){
        if (shippingAddress == null) {
            return null;
        }
        return new ShippingAddressResponse(shippingAddress.getName(), shippingAddress.getPhone(), shippingAddress.getLine1(), shippingAddress.getLine2(), shippingAddress.getCity(), shippingAddress.getState(), shippingAddress.getCountry(), shippingAddress.getPincode());
    }

    private OrderResponse mapToOrderResponse(Order order){
        List<OrderItemResponse> items = new ArrayList<>();
        for(OrderItem item: order.getOrderItems()) {
            items.add(new OrderItemResponse(item.getProductId(), item.getProductName(), item.getQuantity(), item.getUnitPrice(), item.getDiscount(), item.getFinalPrice()));
        }
        return new OrderResponse(order.getId(), order.getCustomerId(), items, order.getStatus(), order.getTotalAmount(), mapToShippingAddressResponse(order.getShippingAddress()), order.getCreatedAt());
    }


    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, InventoryService inventoryService){
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request){
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(OrderStatus.CREATED);
        order.setShippingAddress(mapToShippingAddress(request.getShippingAddress()));

        BigDecimal totalPrice = BigDecimal.ZERO;

        LocalDateTime time = LocalDateTime.now();

        order.setCreatedAt(time);
        order.setUpdatedAt(time);

        for(CreateOrderItemRequest requestItem: request.getItems()){
            Long productId = requestItem.getProductId();

            Inventory inventoryRecord = inventoryService.reserveStock(productId, requestItem.getQuantity());

            if(inventoryRecord.getProduct().getStatus() != ProductStatus.ACTIVE) {
                throw new ProductInactiveException(productId);
            }

            OrderItem item = new OrderItem();
            item.setProductId(productId);
            item.setProductName(inventoryRecord.getProduct().getName());

            item.setQuantity(requestItem.getQuantity());
            item.setUnitPrice(inventoryRecord.getPrice());
            item.setDiscount(BigDecimal.ZERO);

            BigDecimal finalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setFinalPrice(finalPrice);
            order.addOrderItem(item);
            totalPrice = totalPrice.add(finalPrice);
        }
        order.setTotalAmount(totalPrice);

        orderRepository.save(order);
        return mapToOrderResponse(order);
    }

    public Order getOrderById(Long orderId){
       return orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public OrderResponse getOrder(Long orderId){
        return mapToOrderResponse(getOrderById(orderId));
    }

    public List<OrderResponse> getOrders(Long customerId){
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<OrderResponse> responses = new ArrayList<>();

        for(Order order: orders){
            responses.add(mapToOrderResponse(order));
        }
        return responses;
    }

    @Transactional
    public void confirmOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if(order.getStatus()!=OrderStatus.CREATED){
            throw new InvalidOrderStateException(orderId, order.getStatus());
        }
        order.setStatus(OrderStatus.CONFIRMED);

        for(OrderItem orderItem: order.getOrderItems()) {
            Long productId = orderItem.getProductId();
            inventoryService.confirmReservation(productId, orderItem.getQuantity());
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId){
        Order order = getOrderById(orderId);
        if(order.getStatus()!=OrderStatus.CREATED){
            throw new InvalidOrderStateException(orderId, order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);

        for(OrderItem orderItem: order.getOrderItems()) {
            Long productId = orderItem.getProductId();
            inventoryService.releaseReservation(productId, orderItem.getQuantity());
        }

        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        return mapToOrderResponse(order);
    }
}
