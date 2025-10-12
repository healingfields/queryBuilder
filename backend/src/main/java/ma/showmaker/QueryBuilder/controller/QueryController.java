package ma.showmaker.QueryBuilder.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import ma.showmaker.QueryBuilder.dto.FilterDto;
import ma.showmaker.QueryBuilder.dto.QueryRequestDto;
import ma.showmaker.QueryBuilder.dto.QueryResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class QueryController {

    private final EntityManager entityManager;

    public QueryController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping("/query")
    public QueryResponseDto<Object> executeQuery(@RequestBody QueryRequestDto queryRequestDto){
        String table = queryRequestDto.getTable();
        String tablePrefix = table.substring(0, 1).toLowerCase();

        String selectedColumns = (queryRequestDto.getColumns() == null || queryRequestDto.getColumns().isEmpty())
                ? tablePrefix // Return tablePrefix (e.g., "o") when columns are null or empty
                : queryRequestDto.getColumns().stream()
                .map(column -> tablePrefix + "." + "" + column + "") // Prefix and escape column
                .collect(Collectors.joining(", "));
        // [age, name] age, name

        StringBuilder statement = new StringBuilder("select " + selectedColumns + " from " + table + " " + tablePrefix);

        //where name <>=like 'reda'
        //adding filters
        if(queryRequestDto.getFilters() != null || !queryRequestDto.getFilters().isEmpty()){
            statement.append(" where");
            for(int i = 0; i < queryRequestDto.getFilters().size(); i++){
                FilterDto filter = queryRequestDto.getFilters().get(i);

                statement.append(" ")
                        .append(tablePrefix + "." + filter.getColumn())
                        .append(" ")
                        .append(filter.getOperator())
                        .append(" ");
                //this needs to be handled in a better way
                if (filter.getColumn().equalsIgnoreCase("amount") ||
                        filter.getColumn().equalsIgnoreCase("age") || filter.getColumn().equalsIgnoreCase("id")) {
                    statement.append(filter.getValue()); // numeric
                } else {
                    statement.append("'").append(filter.getValue()).append("'"); // string
                }
                if(i < queryRequestDto.getFilters().size() - 1){
                    statement.append(" AND");
                }
            }
        }
        Query query = this.entityManager.createQuery(statement.toString());
        return QueryResponseDto.builder()
                .rows(query.getResultList())
                .query(statement.toString())
                .build();
    }
}
