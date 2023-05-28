package edu.nraj.consumerservice.listner;

import edu.nraj.consumerservice.model.OrderStatus;
import edu.nraj.consumerservice.repository.ConsumerRepository;
import edu.nraj.consumerservice.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class Receiver {

    private final ConsumerRepository consumerRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(id = "consumer", queues = "consumer-queue")
    @Transactional
    public void onMessage(Order order) throws InterruptedException {
        System.out.println("Message Received In Consumer Service Message Listener");

        System.out.println("Processing here to verify consumer...");
        order.setOrderStatus(OrderStatus.CONSUMER_VERIFIED);
        System.out.println("saving the order with state: " + order.getOrderStatus());
        consumerRepository.saveAndFlush(order);

        Thread.sleep(10000);

        rabbitTemplate.convertAndSend("amqp.topic", "saga", order);
    }
}
