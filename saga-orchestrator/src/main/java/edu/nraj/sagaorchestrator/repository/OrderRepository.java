package edu.nraj.sagaorchestrator.repository;

import edu.nraj.sagaorchestrator.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderId(String orderId);
    Order getReferenceByOrderId(String orderId);
}
