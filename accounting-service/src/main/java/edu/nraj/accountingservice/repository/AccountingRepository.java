package edu.nraj.accountingservice.repository;

import edu.nraj.accountingservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingRepository extends JpaRepository<Order, String> {
}
