package edu.nraj.sagaorchestrator.config;

import edu.nraj.sagaorchestrator.model.OrderEvent;
import edu.nraj.sagaorchestrator.model.OrderState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderStatemachineConfigTest {

    @Autowired
    StateMachineFactory<OrderState, OrderEvent> factory;

    @Test
    void stateTransitions(){
        String orderId = String.valueOf(UUID.randomUUID());
        StateMachine<OrderState, OrderEvent> sm = factory.getStateMachine(orderId);
//
        sm.start();
        System.out.println("Current State: " + sm.getState().toString());

        Message<OrderEvent> msg = MessageBuilder.withPayload(OrderEvent.VERIFY_CONSUMER)
                .setHeader("ORDER_ID_HEADER", orderId)
                .build();
//
        sm.sendEvent(msg);
        System.out.println("Current State: " + sm.getState().toString());

        assertEquals(OrderState.CREATING_TICKET, sm.getState());
//
//        msg = MessageBuilder.withPayload(OrderEvent.BOOK_AIRLINE_COMPLETED)
//                .setHeader("ORDER_ID_HEADER", bookingId)
//                .build();
//
//        sm.sendEvent(msg);
//
//        System.out.println("Current State: " + sm.getState().toString());
//
    }

}