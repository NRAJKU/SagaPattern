package edu.nraj.sagaorchestrator.model;

import lombok.*;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_table")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Order {

    @Id
    public String orderId = String.valueOf(UUID.randomUUID());

    @Enumerated(EnumType.STRING)
    public OrderState orderState;

    @Enumerated(EnumType.STRING)
    public OrderEvent orderEvent;

    @Enumerated(EnumType.STRING)
    public OrderStatus orderStatus;
}
