package edu.nraj.sagaorchestrator.listner;

import edu.nraj.sagaorchestrator.dto.OrderCommand;
import edu.nraj.sagaorchestrator.model.Order;
import edu.nraj.sagaorchestrator.model.OrderStatus;
import edu.nraj.sagaorchestrator.repository.OrderRepository;
import edu.nraj.sagaorchestrator.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQueueListener {
    private final OrderService sagaService;

    @Autowired
    OrderRepository orderRepository;

    @RabbitListener(id = "order", queues = "order-queue")
    public void onMessage(OrderCommand message) {
        Order order = orderRepository.findByOrderId(message.orderId);

        switch(order.getOrderStatus()) {
            case CARD_AUTHORIZED:
                order.setOrderStatus(OrderStatus.ORDER_APPROVED);
                System.out.println("Processing here to reject order...");
                System.out.println("saving the order with status: " + order.getOrderStatus());
                orderRepository.saveAndFlush(order);
                break;
            case CARD_NOT_AUTHORIZED:
                order.setOrderStatus(OrderStatus.ORDER_REJECTED);
                System.out.println("Processing here to reject order...");
                System.out.println("saving the order with status: " + order.getOrderStatus());
                orderRepository.saveAndFlush(order);
                break;
            default:
                break;
        }

    }
}
