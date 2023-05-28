package edu.nraj.accountingservice.listner;

import edu.nraj.accountingservice.model.OrderStatus;
import edu.nraj.accountingservice.repository.AccountingRepository;
import edu.nraj.accountingservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class Receiver {

    private final AccountingRepository accountingRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(id = "accounting", queues = "accounting-queue")
    @Transactional
    public void onMessage(Order order) throws InterruptedException {
        System.out.println("Message Received In Accounting Service Message Listener");

        System.out.println("Processing here to verify accounting...");
        order.setOrderStatus(OrderStatus.CARD_AUTHORIZED);
//        order.setOrderStatus(OrderStatus.CARD_NOT_AUTHORIZED);
        System.out.println("saving the order with state : " + order.getOrderStatus());
        accountingRepository.saveAndFlush(order);

        Thread.sleep(10000);

        rabbitTemplate.convertAndSend("amqp.topic", "saga", order);
    }
}
