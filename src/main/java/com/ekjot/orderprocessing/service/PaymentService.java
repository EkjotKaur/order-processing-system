package com.ekjot.orderprocessing.service;

import com.ekjot.orderprocessing.dto.request.CreatePaymentRequest;
import com.ekjot.orderprocessing.dto.response.PaymentResponse;
import com.ekjot.orderprocessing.entity.Order;
import com.ekjot.orderprocessing.entity.Payment;
import com.ekjot.orderprocessing.enums.OrderStatus;
import com.ekjot.orderprocessing.enums.PaymentStatus;
import com.ekjot.orderprocessing.exception.InvalidOrderStateException;
import com.ekjot.orderprocessing.exception.InvalidPaymentStateException;
import com.ekjot.orderprocessing.exception.PaymentAlreadyExistsException;
import com.ekjot.orderprocessing.exception.PaymentNotFoundException;
import com.ekjot.orderprocessing.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    private PaymentResponse mapToPaymentResponse(Payment payment){
        return new PaymentResponse(payment.getId(), payment.getOrderId(), payment.getAmount(), payment.getPaymentMethod(), payment.getStatus(), payment.getPaymentGatewayTransactionId(), payment.getCreatedAt());
    }

    private Payment getPaymentById(Long paymentId){
        return paymentRepository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    private boolean havePendingPayment(Long orderId){
        return paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.PENDING).isPresent();
    }

    private boolean haveSuccessfulPayment(Long orderId){
        return paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.SUCCESS).isPresent();
    }

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService){
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request){
        Long orderId = request.getOrderId();
        Order order = orderService.getOrderById(orderId);
        boolean paymentExists = haveSuccessfulPayment(orderId) || havePendingPayment(orderId);
        if(order.getStatus() != OrderStatus.CREATED){
            throw new InvalidOrderStateException(orderId, order.getStatus());
        }
        if(paymentExists){
            throw new PaymentAlreadyExistsException(orderId);
        }
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentMethod(request.getPaymentMethod());
        LocalDateTime now = LocalDateTime.now();
        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);

        paymentRepository.save(payment);

        return mapToPaymentResponse(payment);
    }

    public PaymentResponse getPayment(Long paymentId){
        Payment payment = getPaymentById(paymentId);
        return mapToPaymentResponse(payment);
    }

    @Transactional
    public PaymentResponse confirmPayment(Long paymentId){
        Payment payment = getPaymentById(paymentId);
        Long orderId = payment.getOrderId();

        if(!havePendingPayment(orderId)){
            throw new InvalidPaymentStateException(paymentId, payment.getStatus());
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentGatewayTransactionId(
                "PAY-" + UUID.randomUUID()
        );
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        orderService.confirmOrder(orderId);
        return mapToPaymentResponse(payment);
    }

    @Transactional
    public PaymentResponse failPayment(Long paymentId){
        Payment payment = getPaymentById(paymentId);
        if(payment.getStatus() != PaymentStatus.PENDING){
            throw new InvalidPaymentStateException(paymentId, payment.getStatus());
        }
        payment.setStatus(PaymentStatus.FAILED);
        payment.setPaymentGatewayTransactionId(
                "PAY-" + UUID.randomUUID()
        );
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        return mapToPaymentResponse(payment);
    }
}
