package ma.showmaker.QueryBuilder.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private Integer age;

    public User(String username, String email, Integer age){
        this.username = username;
        this.email = email;
        this.age = age;
    }
}
