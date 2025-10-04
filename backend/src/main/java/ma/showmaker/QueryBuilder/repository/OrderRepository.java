package ma.showmaker.QueryBuilder.repository;

import ma.showmaker.QueryBuilder.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
