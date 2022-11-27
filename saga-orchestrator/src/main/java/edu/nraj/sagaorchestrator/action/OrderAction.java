package edu.nraj.sagaorchestrator.action;

import edu.nraj.sagaorchestrator.dto.OrderCommand;
import edu.nraj.sagaorchestrator.model.Order;
import edu.nraj.sagaorchestrator.model.OrderEvent;
import edu.nraj.sagaorchestrator.model.OrderState;
import edu.nraj.sagaorchestrator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAction implements Action<OrderState, OrderEvent> {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void execute(StateContext<OrderState, OrderEvent> context) {
        System.out.println("Asking order service to approve/reject order");
        String orderId = (String) context.getMessage().getHeaders().get("ORDER_ID_HEADER");
        Order order = orderRepository.findByOrderId(orderId);
        System.out.println("order id" + order.getOrderId());
        OrderCommand command = new OrderCommand(order.getOrderId(), order.getOrderStatus());

        rabbitTemplate.convertAndSend("amqp.topic", "order", command);
    }
}
