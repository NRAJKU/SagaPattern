package edu.nraj.sagaorchestrator.service;

import edu.nraj.sagaorchestrator.dto.OrderCommand;
import edu.nraj.sagaorchestrator.model.Order;
import edu.nraj.sagaorchestrator.model.OrderEvent;
import edu.nraj.sagaorchestrator.model.OrderState;
import edu.nraj.sagaorchestrator.model.OrderStatus;
import edu.nraj.sagaorchestrator.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SagaService {
    private final OrderRepository orderRepository;
    private final OrderStateChangeInterceptor orderStateChangeInterceptor;
    private final StateMachineFactory<OrderState, OrderEvent> stateMachineFactory;

    @Transactional
    public Order placeOrder(Order order) {
        order.setOrderStatus(OrderStatus.VERIFICATION_PENDING);
        order.setOrderState(OrderState.INITIAL);
        order.setOrderEvent(OrderEvent.VERIFY_CONSUMER);
        Order savedOrder = orderRepository.saveAndFlush(order);

        sendEvent(order);
        return savedOrder;
    }

    @Transactional
    public void processSagaResponse(OrderCommand message) {
        Order order = orderRepository.findByOrderId(message.orderId);
        System.out.println("Message listened");

        switch(message.orderStatus){
            case CONSUMER_VERIFIED:
                System.out.println("Consumer is verified to place order, now process at you end");
                order.setOrderEvent(OrderEvent.CREATE_ORDER_TICKET);
                order.setOrderStatus(message.getOrderStatus());

                sendEvent(orderRepository.saveAndFlush(order));
                break;
            case TICKET_CREATED:
                System.out.println("");
                order.setOrderEvent(OrderEvent.AUTHORIZE_CARD);
                order.setOrderStatus(message.getOrderStatus());

                sendEvent(orderRepository.saveAndFlush(order));
                break;
            case CARD_AUTHORIZED:
                System.out.println("");
                order.setOrderEvent(OrderEvent.APPROVE_ORDER);
                order.setOrderStatus(message.getOrderStatus());

                sendEvent(orderRepository.saveAndFlush(order));
                break;
            case CARD_NOT_AUTHORIZED:
                System.out.println("");
                order.setOrderEvent(OrderEvent.REJECT_ORDER);
                order.setOrderStatus(message.getOrderStatus());

                sendEvent(orderRepository.saveAndFlush(order));
                break;
            case CONSUMER_VERIFICATION_FAILED:
                System.out.println("order rejected by consumer services - consumer verification failed");
                order.setOrderStatus(OrderStatus.REJECTED_CONSUMER_NOT_VERIFIED);

                orderRepository.saveAndFlush(order);
                break;
            case TICKET_NOT_CREATED:
                System.out.println("a");
                order.setOrderStatus(OrderStatus.REJECTED_TICKET_NOT_CREATED);

                orderRepository.saveAndFlush(order);
                break;
            default:
                break;
        }
    }

    private void sendEvent(Order order) {
//        Triggereing state transition by EventTrigger
//        by directly sending events( also called signals) to state machine
        StateMachine<OrderState, OrderEvent> sm = build(order);
        Message<OrderEvent> msg = MessageBuilder.withPayload(order.getOrderEvent())
                .setHeader("ORDER_ID_HEADER", order.getOrderId())
                .build();

        System.out.println("Now sending event: " + order.getOrderEvent().name() + " with current state: " +
                order.getOrderState().name() + " Logging the statemachine state: " + sm.getState());
        sm.sendEvent(msg);
    }


    private StateMachine<OrderState, OrderEvent> build(Order order) {
        StateMachine<OrderState, OrderEvent> sm = stateMachineFactory.getStateMachine(order.getOrderId());
        sm.stop();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(orderStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(
                            order.getOrderState(),
                            null,
                            null,
                            null));

                });
        sm.start();

        System.out.println("State-machine for Order State: " + order.getOrderState());
        return sm;
    }
}
