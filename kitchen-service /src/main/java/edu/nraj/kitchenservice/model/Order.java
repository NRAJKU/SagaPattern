package edu.nraj.kitchenservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    public OrderStatus orderStatus;
}
