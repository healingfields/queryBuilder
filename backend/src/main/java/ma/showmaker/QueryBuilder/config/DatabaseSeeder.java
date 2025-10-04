package ma.showmaker.QueryBuilder.config;

import ma.showmaker.QueryBuilder.model.Order;
import ma.showmaker.QueryBuilder.model.User;
import ma.showmaker.QueryBuilder.repository.OrderRepository;
import ma.showmaker.QueryBuilder.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public DatabaseSeeder(UserRepository userRepository, OrderRepository orderRepositoryr){
        this.orderRepository = orderRepositoryr;
        this.userRepository = userRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("idriss", "idriss@gmail.com", Integer.valueOf(26));
        User user2 = new User("reda", "reda@gmail.com", Integer.valueOf(25));
        User user3 = new User("ali", "adli@gmail.com", Integer.valueOf(27));

        this.userRepository.save(user1);
        this.userRepository.save(user2);
        this.userRepository.save(user3);

        this.orderRepository.save(new Order("bike", 10000.0, LocalDate.now().minusDays(1), user1));
        this.orderRepository.save(new Order("car", 70000.0, LocalDate.now().minusDays(10), user2));
        this.orderRepository.save(new Order("book", 100.0, LocalDate.now().minusDays(5), user3));
        this.orderRepository.save(new Order("kindle", 1200.0, LocalDate.now().minusDays(2), user1));
    }
}
