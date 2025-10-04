package ma.showmaker.QueryBuilder.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ORDERS")
@NoArgsConstructor
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String product;
    private Double amount;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    public Order(String product, Double amount, LocalDate date, User user){
        this.product = product;
        this.amount = amount;
        this.date = date;
        this.user = user;
    }
}
