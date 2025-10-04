package ma.showmaker.QueryBuilder.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ColumnController {

    private final EntityManager entityManager;

    public ColumnController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @GetMapping("/columns")
    public List<String> getTableColumns(@RequestParam String table){
        EntityType<?> entity = this.entityManager
                .getMetamodel()
                .getEntities()
                .stream()
                .filter(entityType -> entityType.getName().equalsIgnoreCase(table))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("cannot find table"));

        return entity
                .getSingularAttributes()
                .stream()
                .map(SingularAttribute::getName)
                .toList();

    }
}
