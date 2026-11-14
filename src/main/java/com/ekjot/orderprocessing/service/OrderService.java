package com.ekjot.orderprocessing.service;

import com.ekjot.orderprocessing.dto.request.CreateOrderItemRequest;
import com.ekjot.orderprocessing.dto.request.CreateOrderRequest;
import com.ekjot.orderprocessing.dto.request.UpdateInventoryStockRequest;
import com.ekjot.orderprocessing.dto.response.InventoryResponse;
import com.ekjot.orderprocessing.dto.response.OrderItemResponse;
import com.ekjot.orderprocessing.dto.response.OrderResponse;
import com.ekjot.orderprocessing.entity.Inventory;
import com.ekjot.orderprocessing.entity.Order;
import com.ekjot.orderprocessing.entity.OrderItem;
import com.ekjot.orderprocessing.entity.Product;
import com.ekjot.orderprocessing.enums.OrderStatus;
import com.ekjot.orderprocessing.enums.ProductStatus;
import com.ekjot.orderprocessing.exception.OrderNotFoundException;
import com.ekjot.orderprocessing.exception.ProductInactiveException;
import com.ekjot.orderprocessing.exception.ProductNotFoundException;
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

    private OrderResponse mapToOrderResponse(Order order){
        List<OrderItemResponse> items = new ArrayList<>();
        for(OrderItem item: order.getOrderItems()) {
            items.add(new OrderItemResponse(item.getProductId(), item.getProductName(), item.getQuantity(), item.getUnitPrice(), item.getDiscount(), item.getFinalPrice()));
        }
        return new OrderResponse(order.getId(), order.getCustomerId(), items, order.getStatus(), order.getTotalAmount(), order.getCreatedAt());
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

    public OrderResponse getOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return mapToOrderResponse(order);
    }

    public List<OrderResponse> getOrders(Long customerId){
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<OrderResponse> responses = new ArrayList<>();

        for(Order order: orders){
            responses.add(mapToOrderResponse(order));
        }
        return responses;
    }
}
