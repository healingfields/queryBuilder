package ma.showmaker.QueryBuilder.repository;

import ma.showmaker.QueryBuilder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
