package com.micro.order.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.micro.order.customer.CustomerClient;
import com.micro.order.exception.BusinessException;
import com.micro.order.product.ProductClient;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;

    public Integer createOrder(@RequestBody @Valid OrderRequest request) {
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));

        productClient.purchaseProducts(request.products());
        return null; // Replace with actual implementation
    }

    public List<OrderResponse> findAllOrders() {
        // Logic to find all orders
        return null; // Replace with actual implementation
    }

    public OrderResponse findById(@PathVariable("order-id") Integer orderId) {
        // Logic to find an order by ID
        return null; // Replace with actual implementation
    }
}
