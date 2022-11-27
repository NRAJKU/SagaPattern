package edu.nraj.consumerservice.repository;

import edu.nraj.consumerservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends JpaRepository<Order, String> {
}
