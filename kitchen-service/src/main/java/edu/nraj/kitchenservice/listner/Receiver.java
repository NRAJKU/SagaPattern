package edu.nraj.kitchenservice.listner;

import edu.nraj.kitchenservice.model.OrderStatus;
import edu.nraj.kitchenservice.repository.KitchenRepository;
import edu.nraj.kitchenservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class Receiver {

    private final KitchenRepository kitchenRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(id = "kitchen", queues = "kitchen-queue")
    @Transactional
    public void onMessage(Order order) throws InterruptedException {
        System.out.println("Message Received In Kitchen Service Message Listener");

        switch(order.orderStatus) {
            case CONSUMER_VERIFIED:
                System.out.println("Processing here to verify kitchen...");
                order.setOrderStatus(OrderStatus.TICKET_CREATED);
//                order.setOrderStatus(OrderStatus.TICKET_NOT_CREATED);
                System.out.println("saving the order with state: " + order.getOrderStatus());
                kitchenRepository.saveAndFlush(order);
                Thread.sleep(10000);
                rabbitTemplate.convertAndSend("amqp.topic", "saga", order);
                break;
            case CARD_AUTHORIZED:
                System.out.println("Card authorized now approve kitchen ticket...");
                order.setOrderStatus(OrderStatus.TICKET_APPROVED);
                System.out.println("saving the order with state: " + order.getOrderStatus());
                kitchenRepository.saveAndFlush(order);
                break;
            case CARD_NOT_AUTHORIZED:
                System.out.println("Card not authorized now reject kitchen ticket...");
                order.setOrderStatus(OrderStatus.TICKET_REJECTED);
                System.out.println("saving the order with state: " + order.getOrderStatus());
                kitchenRepository.saveAndFlush(order);
                break;
            default:
                break;
        }
    }
}
