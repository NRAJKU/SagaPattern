package edu.nraj.sagaorchestrator.service;

import edu.nraj.sagaorchestrator.model.Order;
import edu.nraj.sagaorchestrator.model.OrderEvent;
import edu.nraj.sagaorchestrator.model.OrderState;
import edu.nraj.sagaorchestrator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderState, OrderEvent> {

    private final OrderRepository orderRepository;

    @Override
    public void preStateChange(State<OrderState, OrderEvent> state, Message<OrderEvent> message, Transition<OrderState, OrderEvent> transition,
                               StateMachine<OrderState, OrderEvent> stateMachine, StateMachine<OrderState, OrderEvent> rootStateMachine) {
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault("ORDER_ID_HEADER", "")))
                .ifPresent(orderId -> {
                    System.out.println("Saving state for order id: " + orderId + " state: " + state.getId());
                    Order order = orderRepository.getReferenceByOrderId(orderId);
                    order.setOrderState(state.getId());

                    orderRepository.saveAndFlush(order);// Hibernate is LazeLoading, so to avoid that.
                });
    }
}
