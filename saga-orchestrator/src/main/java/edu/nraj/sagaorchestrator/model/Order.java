package edu.nraj.sagaorchestrator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.EnumType;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_table")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Order {

    @Id
    private String orderId = String.valueOf(UUID.randomUUID());

    @OneToMany
    private List<LineItems> lineItems;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Enumerated(EnumType.STRING)
    private OrderEvent orderEvent;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Order(OrderState state, OrderEvent event, OrderStatus status) {
        orderState = state;
        orderEvent = event;
        orderStatus = status;
    }

//    static Factory method
    public static Order create(){
        return new Order(OrderState.INITIAL, OrderEvent.VERIFY_CONSUMER, OrderStatus.VERIFICATION_PENDING);
    }
}
