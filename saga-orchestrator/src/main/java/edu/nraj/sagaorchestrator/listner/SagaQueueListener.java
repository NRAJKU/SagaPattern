package edu.nraj.sagaorchestrator.listner;

import edu.nraj.sagaorchestrator.dto.OrderCommand;
import edu.nraj.sagaorchestrator.model.Order;
import edu.nraj.sagaorchestrator.model.OrderStatus;
import edu.nraj.sagaorchestrator.repository.OrderRepository;
import edu.nraj.sagaorchestrator.service.SagaService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SagaQueueListener {
    private final SagaService sagaService;

    @Autowired
    OrderRepository orderRepository;

    @RabbitListener(id = "saga", queues = "saga-queue")
    public void onMessage(OrderCommand message) {
        sagaService.processSagaResponse(message);
    }
}
