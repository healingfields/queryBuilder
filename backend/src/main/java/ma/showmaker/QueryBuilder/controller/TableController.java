package ma.showmaker.QueryBuilder.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TableController {

    private final EntityManager entityManager;

    public TableController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GetMapping("/tables")
    public List<String> getTables(){
        return this.entityManager
                .getMetamodel()
                .getEntities()
                .stream()
                .map(EntityType::getName)
                .toList();
    }
}
