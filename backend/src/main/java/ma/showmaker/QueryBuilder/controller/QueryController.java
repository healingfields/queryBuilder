package ma.showmaker.QueryBuilder.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import ma.showmaker.QueryBuilder.dto.FilterDto;
import ma.showmaker.QueryBuilder.dto.QueryRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {

    private final EntityManager entityManager;

    public QueryController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/query")
    public List<?> executeQuery(@RequestBody QueryRequestDto queryRequestDto){
        String selectedColumns = (queryRequestDto.getColumns() == null || queryRequestDto.getColumns().isEmpty())
                ? "*"
                : String.join(", ", queryRequestDto.getColumns());
        // [age, name] age, name

        StringBuilder statement = new StringBuilder("select " + selectedColumns + " from " + queryRequestDto.getTable());

        //where name <>=like 'reda'
        //adding filters
        if(queryRequestDto.getFilters() != null || !queryRequestDto.getFilters().isEmpty()){
            statement.append(" where");
            for(int i = 0; i < queryRequestDto.getFilters().size(); i++){
                FilterDto filter = queryRequestDto.getFilters().get(i);

                statement.append(" ")
                        .append(filter.getColumn())
                        .append(" ")
                        .append(filter.getOperator())
                        .append(" ");
                //this needs to be handled in a better way
                if (filter.getColumn().equalsIgnoreCase("amount") ||
                        filter.getColumn().equalsIgnoreCase("age")) {
                    statement.append(filter.getValue()); // numeric
                } else {
                    statement.append("'").append(filter.getValue()).append("'"); // string
                }
                if(i < queryRequestDto.getFilters().size() - 1){
                    statement.append(" AND");
                }
            }
        }
        Query query = this.entityManager.createNativeQuery(statement.toString());
        return query.getResultList();
    }
}
