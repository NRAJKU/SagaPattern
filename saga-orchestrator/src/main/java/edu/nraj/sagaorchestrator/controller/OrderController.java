package edu.nraj.sagaorchestrator.controller;

import edu.nraj.sagaorchestrator.model.Order;
import edu.nraj.sagaorchestrator.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final OrderService sagaService;

    @PostMapping("/order")
    public ResponseEntity restaurantOrder(@RequestBody Order order) throws Exception {

        Order savedOrder = sagaService.placeOrder(order);
        return ResponseEntity.created(new URI(savedOrder.getOrderId())).build();
    }
}
