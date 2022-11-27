package edu.nraj.kitchenservice.repository;

import edu.nraj.kitchenservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KitchenRepository extends JpaRepository<Order, String> {
}
