package edu.nraj.sagaorchestrator.dto;

import edu.nraj.sagaorchestrator.model.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCommand {

    @Id
    public String orderId = String.valueOf(UUID.randomUUID());

    @Enumerated(EnumType.STRING)
    public OrderStatus orderStatus;
}
