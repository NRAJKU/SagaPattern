package edu.nraj.sagaorchestrator.config;

import edu.nraj.sagaorchestrator.model.OrderEvent;
import edu.nraj.sagaorchestrator.model.OrderState;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@AllArgsConstructor
public class OrderStatemachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

    Action<OrderState, OrderEvent> accountingAction;
    Action<OrderState, OrderEvent> kitchenAction;
    Action<OrderState, OrderEvent> consumerAction;
    Action<OrderState, OrderEvent> orderAction;


    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderState.INITIAL)
                .states(EnumSet.allOf(OrderState.class))
                .end(OrderState.ORDER_APPROVED)
                .end(OrderState.REJECTED);
        /**
         * We can also define state entry and exit actions like this, it is different from transition actions
         * --------
         * We defined an action for the initial state, S1.
         *
         * We defined an entry action for state S1 and left the exit action empty.
         *
         * We defined an exit action for state S2 and left the entry action empty.
         *
         * We defined a single state action for state S2.
         *
         * We defined both entry and exit actions for state S3.
         *
         * Note that state S1 is used twice with initial() and state() functions. You need to do this only if you want to define entry or exit actions with initial state.
         * ---------
         *
         * Defining action with initial() function only runs a particular action when a state machine or sub state is started
         *
         * states
         * 	.withStates()
         * 	    .initial(States.S1, action())
         * 		.state(States.S1, action(), null)
         * 		.state(States.S2, null, action())
         * 		.state(States.S2, action())
         * 		.state(States.S3, action(), action());
         */
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        /**
         * we are defining state's transition (from source to target, which are triggered either by sending event to SM or timers)  actions here
         */
        transitions
                .withExternal()
                    .source(OrderState.INITIAL).target(OrderState.VERIFYING_CONSUMER).event(OrderEvent.VERIFY_CONSUMER)
                    .action(consumerAction)
                .and().withExternal()
                    .source(OrderState.VERIFYING_CONSUMER).target(OrderState.CREATING_TICKET).event(OrderEvent.CREATE_ORDER_TICKET)
                    .action(kitchenAction)
                .and().withExternal()
                    .source(OrderState.CREATING_TICKET).target(OrderState.AUTHORIZING_CARD).event(OrderEvent.AUTHORIZE_CARD)
                    .action(accountingAction)
                .and().withExternal()
                    .source(OrderState.AUTHORIZING_CARD).target(OrderState.ORDER_APPROVED).event(OrderEvent.APPROVE_ORDER)
                    .action(kitchenAction)
                    .action(orderAction)
                .and().withExternal()
                    .source(OrderState.AUTHORIZING_CARD).target(OrderState.REJECTED).event(OrderEvent.REJECT_ORDER)
                    .action(kitchenAction)
                    .action(orderAction);
    }

}
