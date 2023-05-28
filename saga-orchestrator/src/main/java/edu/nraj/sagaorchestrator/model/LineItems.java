package edu.nraj.sagaorchestrator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Embedded;

@Entity
@Table(name="order_line_items")
@Getter
public class LineItems {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String item;

    @Embedded
    private Money price;

    protected LineItems(){}

    private LineItems(String item, Money price) {
        this.item = item;
        // do some validation on Money here
        this.price = price;
    }

    public static LineItems create(String item, Money price){
        return new LineItems(item, price);
    }
}
